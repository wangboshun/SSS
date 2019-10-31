using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

using SSS.Domain.DigitalCurrency;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.DateTime;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Application.DigitalCurrency.Job
{
    [DIService(ServiceLifetime.Transient, typeof(IHostedService))]
    public class DigitalCurrencyJob : IHostedService, IDisposable
    {
        private readonly ILogger _logger;
        private readonly IServiceScopeFactory _scopeFactory;
        private Timer _timer;
        private readonly List<Domain.DigitalCurrency.DigitalCurrency> ListCoin = new List<Domain.DigitalCurrency.DigitalCurrency>();

        public DigitalCurrencyJob(ILogger<DigitalCurrencyJob> logger, IServiceScopeFactory scopeFactory)
        {
            _logger = logger;
            _scopeFactory = scopeFactory;
        }

        public Task StartAsync(CancellationToken stoppingToken)
        {
            _timer = new Timer(DoWork, null, TimeSpan.Zero,
                TimeSpan.FromMinutes(10));

            return Task.CompletedTask;
        }

        private void DoWork(object state)
        {
            //OutDay();
        }

        public Task StopAsync(CancellationToken stoppingToken)
        {
            _timer?.Change(Timeout.Infinite, 0);

            return Task.CompletedTask;
        }

        public void Dispose()
        {
            _timer?.Dispose();
        }

        /// <summary>
        /// 计算日线
        /// </summary>
        public void OutDay()
        {
            System.Console.WriteLine("---OutDay---");
            List<CoinSymbols> allcoin = GetAllCoin();

            foreach (var coin in allcoin)
            {
                Calc(coin.base_currency, coin.quote_currency, CoinTime.Time_1day);
            }

            using var scope = _scopeFactory.CreateScope();
            using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();

            if (ListCoin.Any())
            {
                context.Database.ExecuteSqlRaw("UPDATE DigitalCurrency SET IsDelete=1");
                context.DigitalCurrency.AddRange(ListCoin);
                context.SaveChangesAsync();
                ListCoin.Clear();
                System.Console.WriteLine("---OutDay  SaveChangesAsync---");
            }
        }

        /// <summary>
        /// 获取币币的Logo
        /// </summary>
        /// <returns></returns>
        public string GetLogo(string coin)
        {
            string logo = "https://s1.bqiapp.com/coin/20181030_72_png/bitcoin_200_200.png?v=1566978037";
            try
            {
                using var scope = _scopeFactory.CreateScope();
                using var context = scope.ServiceProvider.GetRequiredService<DbcontextBase>();
                var info = context.CoinInfo.FirstOrDefault(x => x.Coin.Equals(coin.ToUpper()));
                if (info != null)
                    return info.RomteLogo;
                return logo;
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetLogo---");
                return logo;
            }
        }

        /// <summary>
        /// 开始计算
        /// </summary>
        /// <param name="type"></param>
        public void Calc(string base_currency, string quote_currency, CoinTime type)
        {
            System.Console.WriteLine("---Calc---" + base_currency);

            try
            {
                int size = type == CoinTime.Time4_60min ? 800 : 200;

                var kline = GetKLine(base_currency + quote_currency, type.ToString().Split('_')[1], size);

                if (kline == null || kline.Count < 1)
                    return;

                if (type == CoinTime.Time4_60min)
                    kline = Cacl4Hour(kline);

                var data5 = CalcList(kline, 5);
                var data10 = CalcList(kline, 10);
                var data30 = CalcList(kline, 30);
                var data60 = CalcList(kline, 60);

                //获取时间段
                string typename = GetTimeType(type);

                if (data5.Count > 0 && data10.Count > 0 && data5.First().price > data10.First().price)
                {
                    Domain.DigitalCurrency.DigitalCurrency model =
                        new Domain.DigitalCurrency.DigitalCurrency()
                        {
                            Id = Guid.NewGuid().ToString(),
                            Coin = base_currency.ToUpper() + "-" + quote_currency.ToUpper(),
                            TimeType = typename,
                            CreateTime = DateTime.Now,
                            Platform = "火币",
                            Close = kline.First().close,
                            Open = kline.First().open,
                            High = kline.First().high,
                            Low = kline.First().low
                        };

                    if (data5.Count > 0 && data30.Count > 0 && data5.First().price > data30.First().price)
                    {
                        if (data5.Count > 0 && data60.Count > 0 && data5.First().price > data60.First().price)
                        {
                            System.Console.WriteLine(base_currency.ToUpper() + "—" + quote_currency.ToUpper() + $"【{typename}】突破60K压力位,金叉");
                            model.Desc = "突破60K压力位,金叉";
                        }
                        else
                        {
                            System.Console.WriteLine(base_currency.ToUpper() + "—" + quote_currency.ToUpper() + $"【{typename}】突破30K压力位,金叉");
                            model.Desc = "突破30K压力位,金叉";
                        }
                    }
                    else
                    {
                        model.Desc = "突破10K压力位,金叉";
                        System.Console.WriteLine(base_currency.ToUpper() + "—" + quote_currency.ToUpper() + $"【{typename}】突破10K压力位,金叉");
                    }

                    model.HighRange = (model.High / model.Low) - 1;
                    model.CloseRange = (model.Close / model.Open) - 1;
                    model.Logo = GetLogo(base_currency);
                    ListCoin.Add(model);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Calc---");
            }
        }

        /// <summary>
        /// 获取时间段
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public string GetTimeType(CoinTime type)
        {
            switch (type)
            {
                case CoinTime.Time_15min:
                    return "15分钟";

                case CoinTime.Time_60min:
                    return "1小时";

                case CoinTime.Time4_60min:
                    return "4小时";

                case CoinTime.Time_1day:
                    return "日线";
            }

            return "";
        }

        /// <summary>
        /// 计算四小时
        /// </summary>
        /// <param name="list"></param>
        /// <returns></returns>
        public List<KLine> Cacl4Hour(List<KLine> list)
        {
            try
            {
                List<KLine> templist = new List<KLine>();
                int index = 0;
                int hour = list.First().time.Hour;

                if (hour >= 0 && hour < 4)
                {
                    index = hour - 0;
                }
                else if (hour >= 4 && hour < 8)
                {
                    index = hour - 4;
                }
                else if (hour >= 8 && hour < 12)
                {
                    index = hour - 8;
                }
                else if (hour >= 12 && hour < 16)
                {
                    index = hour - 12;
                }
                else if (hour >= 16 && hour < 20)
                {
                    index = hour - 16;
                }
                else if (hour >= 20 && hour < 24)
                {
                    index = hour - 20;
                }

                var temp = list.Skip(0).Take(index + 1).ToList();

                templist.Add(new KLine()
                {
                    open = temp.Last().open,
                    close = temp.First().close,
                    high = temp.Max(x => x.high),
                    count = temp.Sum(x => x.count),
                    vol = temp.Sum(x => x.vol),
                    amount = temp.Sum(x => x.amount),
                    id = temp.Last().id,
                    low = temp.Min(x => x.low),
                    time = temp.Last().time
                });


                var val = SpiltList<KLine>(list.Skip(index + 1).Take(list.Count).ToList(), 4);

                for (int i = 0; i < val.Count; i++)
                {
                    templist.Add(new KLine()
                    {
                        open = val[i].Last().open,
                        close = val[i].First().close,
                        high = val[i].Max(x => x.high),
                        count = val[i].Sum(x => x.count),
                        vol = val[i].Sum(x => x.vol),
                        amount = val[i].Sum(x => x.amount),
                        id = val[i].Last().id,
                        low = val[i].Min(x => x.low),
                        time = val[i].Last().time
                    });
                }

                return templist;
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---Cacl4Hour---");
                return null;
            }
        }

        /// <summary>
        /// 分组
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="Lists"></param>
        /// <param name="num"></param>
        /// <returns></returns>
        public List<List<T>> SpiltList<T>(List<T> Lists, int num) //where T:class
        {
            return Lists
                .Select((x, i) => new { Index = i, Value = x })
                .GroupBy(x => x.Index / num)
                .Select(x => x.Select(v => v.Value).ToList())
                .ToList();
        }

        /// <summary>
        /// 计算均价
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        public List<AvgPrice> CalcList(List<KLine> data, int size) //where T:class
        {
            List<AvgPrice> list = new List<AvgPrice>();

            for (int i = 0; i < data.Count; i++)
            {
                if (data.Skip(i).Take(size).Count() < size)
                    return list;

                AvgPrice price = new AvgPrice
                {
                    time = data[i].time,
                    price = data.Skip(i).Take(size).Sum(x => x.close) / size
                };

                list.Add(price);
            }
            return list;
        }


        /// <summary>
        /// 获取所有USDT交易对
        /// </summary>
        /// <returns></returns>
        public List<CoinSymbols> GetAllCoin()
        {
            List<CoinSymbols> list = new List<CoinSymbols>();

            try
            {
                WebClient http = new WebClient();

                string result = http.DownloadString("https://api.huobi.vn/v1/common/symbols");

                JObject json_root = (JObject)JsonConvert.DeserializeObject(result);

                var json_data = json_root["data"];
                foreach (var item in json_data)
                {
                    if (item["quote-currency"].ToString().Contains("usdt"))
                    {
                        CoinSymbols s = new CoinSymbols();

                        s.base_currency = item["base-currency"].ToString();
                        s.quote_currency = item["quote-currency"].ToString();
                        list.Add(s);
                    }
                }

                return list;
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetAllCoin---");
                return list;
            }
        }

        /// <summary>
        /// 获取K线
        /// </summary>
        /// <param name="type"></param>
        /// <param name="size"></param>
        /// <returns></returns>
        public List<KLine> GetKLine(string coin, string type, int size)
        {
            List<KLine> list = new List<KLine>();
            try
            {
                WebClient http = new WebClient();

                string result = http.DownloadString($"https://api.huobi.vn/market/history/kline?period={type}&size={size}&symbol={coin}");

                JObject json_root = (JObject)JsonConvert.DeserializeObject(result);

                if (json_root.GetValue("status").ToString().Equals("error"))
                    return null;

                list = JsonConvert.DeserializeObject<List<KLine>>(json_root["data"].ToString());

                foreach (var item in list)
                {
                    item.time = DateTimeConvert.ConvertIntDateTime(item.id);
                }

                return list;
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, "---GetKLine---");
                return list;
            }
        }
    }
}
