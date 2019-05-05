﻿using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using SSS.Application.OkexSdk.Sdk;
using SSS.Application.OkexSdk.Spot;
using SSS.Domain.Okex.Target;

namespace SSS.Application.OkexSdk.Core
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

        /// <summary>
        /// 当前时间的区间k线
        /// </summary>
        /// <param name="time"></param>
        /// <param name="timetype"></param>
        /// <returns></returns>
        public DateTime StartTime(DateTime time, KLineTime timetype)
        {
            switch (timetype)
            {
                case KLineTime.一分钟:
                    return time;
                case KLineTime.十五分钟:
                    if (time.Minute < 15)
                        return new DateTime(time.Year, time.Month, time.Day, time.Hour, 15, 00);
                    if (time.Minute < 30)
                        return new DateTime(time.Year, time.Month, time.Day, time.Hour, 30, 00);
                    if (time.Minute < 45)
                        return new DateTime(time.Year, time.Month, time.Day, time.Hour, 45, 00);
                    if (time.Minute > 45)
                        return new DateTime(time.Year, time.Month, time.Day, time.Hour, 00, 00).AddHours(1);
                    break;
                case KLineTime.一小时:
                    return new DateTime(time.Year, time.Month, time.Day, time.Hour, 00, 00);
                case KLineTime.一天:
                    return new DateTime(time.Year, time.Month, time.Day);
            }
            return time;
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
            try
            {
                SpotApi spotApi = new SpotApi("", "", "");

                DateTime defaulttime = DateTime.UtcNow;

                if (!string.IsNullOrWhiteSpace(begintime))
                    defaulttime = Convert.ToDateTime(begintime);

                DateTime starttime = defaulttime.AddMinutes(-10 * (time / 60));
                DateTime endtime = defaulttime.AddMilliseconds(1);

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
                _logger.LogInformation($"kdata:{JsonConvert.SerializeObject(list)}");
                return list;
            }
            catch (Exception e)
            {
                _logger.LogError(e, "获取K线数据异常");
                return null;
            }
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
                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(macd)} 零轴以上金叉");
                return true;
            }

            if (macd.macd > 0 && macd.dea < 0 && macd.dif < 0)
            {
                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(macd)} 零轴以下金叉");
                return true;
            }

            if (macd.macd > 0)
            {
                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(macd)} 单纯金叉");
                return true;
            }

            if (macd.macd < 0 && macd.dea > 0 && macd.dif > 0)
            {
                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(macd)} 零轴以上死叉");
                return true;
            }

            if (macd.macd < 0 && macd.dea < 0 && macd.dif < 0)
            {
                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(macd)} 零轴以上死叉");
                return true;
            }

            if (macd.macd < 0)
            {
                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(macd)} 单纯死叉");
                return true;
            }

            return false;
        }

        /// <summary>
        /// 获取MACD  DIFF=今日EMA（12）- 今日EMA（26） DEA=前一日DEA×8/10＋今日DIF×2/10 MACD=BAR=2×(DIFF－DEA) 
        /// <param name="instrument">交易对</param>
        /// <param name="close">收盘价</param>
        /// <param name="timetype">时间线</param>
        /// <param name="ema12">ema12</param>
        /// <param name="ema26">ema26</param> 
        /// <param name="ktime">k线时间</param>
        /// <param name="yesday_macd">k线时间</param>
        /// <returns></returns>
        /// </summary>
        public Macd GetMACD(string instrument, double close, int timetype, double ema12, double ema26, DateTime ktime, Macd yesday_macd)
        {
            try
            {
                double dif = ema12 - ema26;
                double dea = yesday_macd.dea * 8 / 10 + dif * 2 / 10;
                double macd = 2 * (dif - dea);

                Macd result = new Macd
                { 
                    yesday_dea = yesday_macd.dea,
                    ktime = ktime,
                    createtime = DateTime.Now,
                    Id = Guid.NewGuid(),
                    timetype = timetype,
                    instrument = instrument,
                    dea = dea,
                    macd = macd,
                    dif = dif,
                    ema12 = ema12,
                    ema26 = ema26
                };

                _logger.LogInformation($"macd:{JsonConvert.SerializeObject(result)}");
                return result;
            }
            catch (Exception e)
            {
                _logger.LogError(e, "获取MACD异常");
                return null;
            }
        }

        #endregion

        #region EMA

        /// <summary>
        /// 获取 指数移动平均值 EMA
        /// 当日指数平均值 = 平滑系数*（当日指数值-昨日指数平均值）+昨日指数平均值；平滑系数=2/（周期单位+1）
        /// </summary>
        public Ema GetEMA(string instrument, int timetype, int type, double close, double yesday_ema, DateTime ktime)
        {
            try
            {
                double value = (2.0 / (type + 1.0)) * (close - yesday_ema) + yesday_ema;

                Ema ema = new Ema
                {
                    createtime = DateTime.Now,
                    instrument = instrument,
                    timetype = timetype,
                    parameter = type,
                    yesday_ema = yesday_ema,
                    Id = Guid.NewGuid(),
                    ktime = ktime,
                    now_ema = value
                };
                _logger.LogInformation($"ema:{JsonConvert.SerializeObject(ema)}");
                return ema;
            }
            catch (Exception e)
            {
                _logger.LogError(e, "获取EMA异常");
                return null;
            }
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
        public bool MaPrice_Cross(List<SpotCandle> kdata, string instrument, int min = 5, int max = 10)
        {
            double ma1 = GetMaPrice(kdata, instrument, min).now_ma;
            double ma2 = GetMaPrice(kdata, instrument, max).now_ma;
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
        public Ma GetMaPrice(List<SpotCandle> kdata, string instrument, int time, int parameter = 5)
        {
            try
            {
                if (kdata == null && kdata.Count < 0)
                {
                    _logger.LogError($"获取均价线失败，kdata为空");
                    return null;
                }

                List<SpotCandle> list = new List<SpotCandle>(kdata.Skip(0).Take(parameter).ToArray());

                Ma ma = new Ma();
                ma.ktime = list[0].time;

                var value = list.Sum(x => x.close) / list.Count;
                ma.createtime = DateTime.Now;
                ma.instrument = instrument;
                ma.now_ma = value;
                ma.timetype = time;
                ma.parameter = parameter;
                ma.type = 2;

                _logger.LogInformation($"均价线 ma:{JsonConvert.SerializeObject(ma)}");
                return ma;
            }
            catch (Exception e)
            {
                _logger.LogError(e, "获取均价线异常");
                return null;
            }
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
        public bool MaVolume_Cross(List<SpotCandle> kdata, string instrument, int min = 5, int max = 10)
        {
            double ma1 = GetMaVolume(kdata, instrument, min).now_ma;
            double ma2 = GetMaVolume(kdata, instrument, max).now_ma;
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
        public Ma GetMaVolume(List<SpotCandle> kdata, string instrument, int time, int parameter = 5)
        {
            try
            {
                if (kdata == null && kdata.Count < 0)
                {
                    _logger.LogError($"获取均量线失败，kdata为空");
                    return null;
                }

                List<SpotCandle> list = new List<SpotCandle>(kdata.Skip(0).Take(parameter).ToArray());

                Ma ma = new Ma();
                ma.ktime = list[0].time;

                var value = list.Sum(x => x.volume) / list.Count;
                ma.createtime = DateTime.Now;
                ma.instrument = instrument;
                ma.now_ma = value;
                ma.timetype = time;
                ma.parameter = parameter;
                ma.type = 1;

                _logger.LogInformation($"均量线 ma:{JsonConvert.SerializeObject(ma)}");
                return ma;
            }
            catch (Exception e)
            {
                _logger.LogError(e, "获取均量线异常");
                return null;
            }
        }

        #endregion

        #region KDJ

        /// <summary>
        /// 获取KDJ
        /// </summary>
        /// <param name="time">时间</param>
        /// <param name="yesday_kdj">昨日KDJ</param> 
        public Kdj GetKdj(List<SpotCandle> kdata, string instrument, int time, Kdj yesday_kdj, int parameter = 9)
        {
            try
            {
                if (kdata == null && kdata.Count < 0)
                {
                    _logger.LogError($"获取KDJ失败，kdata为空");
                    return null;
                }

                List<SpotCandle> list = new List<SpotCandle>(kdata.Skip(0).Take(parameter).ToArray());

                var low = list.Min(x => x.low);
                var high = list.Max(x => x.high);

                double rsv = RSV(list[0].close, low, high);
                double k = K(rsv, yesday_kdj.k);
                double d = D(yesday_kdj.d, k);
                double j = J(k, d);

                Kdj kdj = new Kdj
                {
                    instrument = instrument,
                    timetype = time,
                    createtime = DateTime.Now,
                    ktime = list[0].time,
                    k = k,
                    d = d,
                    j = j,
                    yesday_d = yesday_kdj.d,
                    yesday_k = yesday_kdj.k
                };

                _logger.LogInformation($"kdj:{JsonConvert.SerializeObject(kdj)}");
                return kdj;
            }
            catch (Exception e)
            {
                _logger.LogError(e, "获取KDJ异常");
                return null;
            }
        }


        /// <summary>
        ///RSV={(Cn－Ln)/(Hn－Ln)}×100
        /// </summary>
        /// <param name="cn">收盘价</param>
        /// <param name="ln">9日最低价</param>
        /// <param name="hn">9日最高价</param>
        /// <returns></returns>
        private double RSV(double cn, double ln, double hn)
        {
            return ((cn - ln) / (hn - ln)) * 100;
        }

        /// <summary>
        /// 2/3×前一日K值+1/3×当日RSV
        /// </summary>
        /// <param name="rsv">rsv</param>
        /// <param name="yesdayK">昨日K</param>
        /// <returns></returns>
        private double K(double rsv, double yesdayK)
        {
            return (2.0 / 3.0) * yesdayK + (1.0 / 3.0) * rsv;
        }

        /// <summary>
        /// 2/3×前一日D值+1/3×当日K值
        /// </summary>
        /// <param name="yesdayD">昨日D</param>
        /// <param name="k">当日K值</param>
        /// <returns></returns>
        private double D(double yesdayD, double k)
        {
            return ((2.0 / 3.0) * yesdayD) + ((1.0 / 3.0) * k);
        }

        /// <summary>
        /// 3*当日K值-2*当日D值
        /// </summary>
        /// <param name="k">当日K值</param>
        /// <param name="d">当日D值</param>
        /// <returns></returns>
        private double J(double k, double d)
        {
            return (3.0 * k) - (2.0 * d);
        }

        #endregion
    }
}