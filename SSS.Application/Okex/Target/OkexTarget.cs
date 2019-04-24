﻿using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Extensions.Logging;
using MongoDB.Bson;
using Newtonsoft.Json.Linq;
using SSS.Application.OkexSdk.Sdk;
using SSS.Domain.Okex.Sdk.Spot;
using SSS.Domain.Okex.Target;

namespace SSS.Application.Okex.Target
{
    /// <summary>
    /// Okex量化指标
    /// </summary>
    public class OkexTarget
    {
        private readonly ILogger _logger;

        public OkexTarget(ILogger<OkexTarget> logger)
        {
            _logger = logger;
        }

        #region K线数据

        /// <summary>
        /// K线数据,单位：秒
        /// </summary>
        /// <param name="instrument">交易对</param>
        /// <param name="time">分钟线</param>
        /// <param name="begintime">起始时间，默认当前时间</param>
        /// <returns></returns>
        public List<SpotCandle> GetKLineData(string instrument, int time, string begintime = "")
        {
            SpotApi spotApi = new SpotApi("", "", "");

            DateTime defaulttime = DateTime.UtcNow;

            if (!string.IsNullOrWhiteSpace(begintime))
                defaulttime = Convert.ToDateTime(begintime);

            DateTime starttime = defaulttime.AddMinutes(-10 * (time / 60));
            DateTime endtime = defaulttime;

            var jcontainer = spotApi.getCandlesAsync(instrument, starttime, endtime, time).Result;

            List<SpotCandle> list = new List<SpotCandle>();

            for (int i = 0; i < jcontainer.Count(); i++)
            {
                var jtoken = jcontainer[i].Values().ToList();
                SpotCandle spot = new SpotCandle();
                spot.instrument = instrument;

                for (int j = 0; j < jtoken.Count; j++)
                {
                    switch (j)
                    {
                        case 0:
                            spot.time = Convert.ToDateTime(jtoken[j]).AddHours(8);
                            break;
                        case 1:
                            spot.open = Convert.ToDouble(jtoken[j]);
                            break;
                        case 2:
                            spot.high = Convert.ToDouble(jtoken[j]);
                            break;
                        case 3:
                            spot.low = Convert.ToDouble(jtoken[j]);
                            break;
                        case 4:
                            spot.close = Convert.ToDouble(jtoken[j]);
                            break;
                        case 5:
                            spot.volume = Convert.ToDouble(jtoken[j]);
                            break;
                    }
                }
                list.Add(spot);
            }
            _logger.LogInformation($"kdata:{list.ToJson()}");
            return list;
        }

        #endregion

        #region MACD

        /// <summary>
        /// macd状态 金叉或死叉
        /// </summary>
        public bool MACD_Cross(Macd macd)
        {
            if (macd.macd > 0 && macd.dea > 0 && macd.dif > 0)
            {
                _logger.LogInformation($"macd:{macd.ToJson()} 零轴以上金叉");
                return true;
            }

            if (macd.macd > 0 && macd.dea < 0 && macd.dif < 0)
            {
                _logger.LogInformation($"macd:{macd.ToJson()} 零轴以下金叉");
                return true;
            }

            if (macd.macd > 0)
            {
                _logger.LogInformation($"macd:{macd.ToJson()} 单纯金叉");
                return true;
            }

            if (macd.macd < 0 && macd.dea > 0 && macd.dif > 0)
            {
                _logger.LogInformation($"macd:{macd.ToJson()} 零轴以上死叉");
                return true;
            }

            if (macd.macd < 0 && macd.dea < 0 && macd.dif < 0)
            {
                _logger.LogInformation($"macd:{macd.ToJson()} 零轴以上死叉");
                return true;
            }

            if (macd.macd < 0)
            {
                _logger.LogInformation($"macd:{macd.ToJson()} 单纯死叉");
                return true;
            }

            return false;
        }

        /// <summary>
        /// 获取 MACD  DIFF=今日EMA（12）- 今日EMA（26） DEA=前一日DEA×8/10＋今日DIF×2/10 MACD=BAR=2×(DIFF－DEA) 
        /// <param name="instrument">交易对</param>
        /// <param name="close">收盘价</param>
        /// <param name="yesday_ema12">昨日ema12</param>
        /// <param name="yesday_ema26">昨日ema26</param>
        /// <param name="yesday_dea">昨日dea</param>
        /// <returns></returns>
        /// </summary>
        public Macd GetMACD(string instrument, double close, double yesday_ema12, double yesday_ema26, double yesday_dea)
        {
            double ema12 = GetEMA(12, close, yesday_ema12);
            double ema26 = GetEMA(26, close, yesday_ema26);

            double dif = ema12 - ema26;
            double dea = yesday_dea * 8 / 10 + dif * 2 / 10;
            double macd = 2 * (dif - dea);

            Macd result = new Macd() { instrument = instrument, dea = dea, macd = macd, dif = dif, ema12 = ema12, ema26 = ema26 };
            _logger.LogInformation($"macd:{result.ToJson()} 零轴以上死叉");
            return result;
        }

        #endregion

        #region EMA

        /// <summary>
        /// 获取 指数移动平均值 EMA
        /// 当日指数平均值 = 平滑系数*（当日指数值-昨日指数平均值）+昨日指数平均值；平滑系数=2/（周期单位+1）
        /// </summary>
        public double GetEMA(double time, double close, double yesday_ema)
        {
            return (2.0 / (time + 1.0)) * (close - yesday_ema) + yesday_ema;
        }

        #endregion 

        #region 均价线

        /// <summary>
        /// 均价线金叉或死叉,默认5，10日线
        /// </summary>
        /// <param name="instrument">交易对</param>
        /// <param name="min">5</param>
        /// <param name="max">10</param>
        /// <returns></returns>
        public bool MaPrice_Cross_5_10(string instrument, int min = 5, int max = 10)
        {
            double ma1 = GetMaPrice(instrument, min);
            double ma2 = GetMaPrice(instrument, max);
            if (ma1 > ma2)
            {
                _logger.LogInformation($"ma1:{ma1} ma2:{ma2} 均价线金叉");
                return true;
            }
            _logger.LogInformation($"ma1:{ma1} ma2:{ma2} 均价线死叉");
            return false;
        }

        /// <summary>
        /// 获取 均价线 MA Price  MA（N）=第1日收盘价+第2日收盘价+………………+第N日收盘价/N
        /// </summary>
        public double GetMaPrice(string instrument, int time)
        {
            var kdata = GetKLineData(instrument, time);
            var ma = kdata.Sum(x => x.close) / kdata.Count;
            _logger.LogInformation($"均价线 ma:{ma}");
            return ma;
        }

        #endregion

        #region 均量线

        /// <summary>
        /// 均量线金叉或死叉,默认5，10日线
        /// </summary>
        /// <param name="instrument">交易对</param>
        /// <param name="min">5</param>
        /// <param name="max">10</param>
        /// <returns></returns>
        public bool MaVolume_Cross_5_10(string instrument, int min = 5, int max = 10)
        {
            double ma1 = GetMaVolume(instrument, min);
            double ma2 = GetMaVolume(instrument, max);
            if (ma1 > ma2)
            {
                _logger.LogInformation($"ma1:{ma1} ma2:{ma2} 均量线金叉");
                return true;
            }
            _logger.LogInformation($"ma1:{ma1} ma2:{ma2} 均量线死叉");
            return false;
        }

        /// <summary>
        /// 获取 均量线 MA Volume  MA（N）=第1日收盘量+第2日收盘量+………………+第N日收盘量/N
        /// </summary>
        public double GetMaVolume(string instrument, int time)
        {
            var kdata = GetKLineData(instrument, time);
            var ma = kdata.Sum(x => x.volume) / kdata.Count;
            _logger.LogInformation($"均量线 ma:{ma}");
            return ma;
        }

        #endregion

        #region KDJ

        /// <summary>
        /// 获取KDJ
        /// </summary>
        /// <param name="time">时间</param>
        /// <param name="yesdayK">昨日K</param>
        /// <param name="yesdayD">昨日D</param>
        public Kdj GetKdj(string instrument, int time, double yesdayK, double yesdayD)
        {
            var kdata = GetKLineData(instrument, time);
            var low = kdata.Min(x => x.low);
            var high = kdata.Max(x => x.high);

            double rsv = RSV(kdata[0].close, low, high);
            double k = K(rsv, yesdayK);
            double d = D(yesdayD, k);
            double j = J(k, d);
            Kdj kdj = new Kdj { instrument = instrument, time = time, createtime = kdata[0].time, k = k, d = d, j = j };
            _logger.LogInformation($"kdj:{kdj.ToJson()}");
            return kdj;
        }


        /// <summary>
        ///RSV={(Cn－Ln)/(Hn－Ln)}×100
        /// </summary>
        /// <param name="cn">收盘价</param>
        /// <param name="ln">9日最低价</param>
        /// <param name="hn">9日最高价</param>
        /// <returns></returns>
        public double RSV(double cn, double ln, double hn)
        {
            return ((cn - ln) / (hn - ln)) * 100;
        }

        /// <summary>
        /// 2/3×前一日K值+1/3×当日RSV
        /// </summary>
        /// <param name="rsv">rsv</param>
        /// <param name="yesdayK">昨日K</param>
        /// <returns></returns>
        public double K(double rsv, double yesdayK)
        {
            return (2.0 / 3.0) * yesdayK + (1.0 / 3.0) * rsv;
        }

        /// <summary>
        /// 2/3×前一日D值+1/3×当日K值
        /// </summary>
        /// <param name="yesdayD">昨日D</param>
        /// <param name="k">当日K值</param>
        /// <returns></returns>
        public double D(double yesdayD, double k)
        {
            return ((2.0 / 3.0) * yesdayD) + ((1.0 / 3.0) * k);
        }

        /// <summary>
        /// 3*当日K值-2*当日D值
        /// </summary>
        /// <param name="k">当日K值</param>
        /// <param name="d">当日D值</param>
        /// <returns></returns>
        public double J(double k, double d)
        {
            return (3.0 * k) - (2.0 * d);
        }

        #endregion
    }
}
