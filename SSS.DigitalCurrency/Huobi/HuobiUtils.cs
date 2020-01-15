using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

using SSS.DigitalCurrency.Domain;
using SSS.Domain.Coin.CoinKLineData;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Config;
using SSS.Infrastructure.Util.DateTime;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;

namespace SSS.DigitalCurrency.Huobi
{
    [DIService(ServiceLifetime.Singleton, typeof(HuobiUtils))]
    public class HuobiUtils
    {
        private readonly ILogger _logger;

        public HuobiUtils(ILogger<HuobiUtils> logger)
        {
            _logger = logger;
        }

        /// <summary>
        ///     获取所有USDT交易对
        /// </summary>
        /// <returns></returns>
        public List<CoinSymbols> GetAllCoin()
        {
            var list = new List<CoinSymbols>();

            try
            {
                var http = new WebClient();

                var result = http.DownloadString("https://api.huobiasia.vip/v1/common/symbols");

                var json_root = (JObject)JsonConvert.DeserializeObject(result);

                var json_data = json_root["data"];

                foreach (var item in json_data)
                    if (item.Value<string>("quote-currency").Contains("usdt"))
                    {
                        var s = new CoinSymbols
                        {
                            base_currency = item["base-currency"]?.ToString(),
                            quote_currency = item["quote-currency"]?.ToString()
                        };
                        list.Add(s);
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
        ///     获取K线
        /// </summary>
        /// <param name="time"></param>
        /// <param name="size"></param>
        /// <returns></returns>
        public List<CoinKLineData> GetKLine(string coin, string quote, string time, int size)
        {
            try
            {
                var http = new WebClient();
                var result = http.DownloadString($"https://api.huobiasia.vip/market/history/kline?period={time}&size={size}&symbol={coin + quote}");

                if (string.IsNullOrWhiteSpace(result)) return null;

                var jobject = JObject.Parse(result);

                var json = jobject?["data"];

                var kline = json?.Select(item => new CoinKLineData
                {
                    Id = Guid.NewGuid().ToString(),
                    IsDelete = 0,
                    Platform = (int)Platform.Huobi,
                    Coin = coin,
                    DataTime = DateTimeConvert.ConvertDateTime(item["id"].ToString()),
                    Open = Convert.ToDouble(item["open"]),
                    Close = Convert.ToDouble(item["close"]),
                    Low = Convert.ToDouble(item["low"]),
                    High = Convert.ToDouble(item["high"]),
                    CreateTime = DateTime.Now
                }).ToList();
                return kline;
            }
            catch (WebException ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, $"---GetKLine {DateTime.Now}---");
                throw;
            }
        }

        /// <summary>
        ///     获取K线
        /// </summary>
        /// <param name="time"></param>
        /// <param name="size"></param>
        /// <returns></returns>
        public string GetKLine(string coin, string time, int size)
        {
            try
            {
                var http = new WebClient();
                //return http.DownloadString($"https://api.huobi.pro/market/history/kline?period={time}&size={size}&symbol={coin}");
                string url = $"{JsonConfig.GetSectionValue("TradeConfig:Api:Huobi")}/market/history/kline?period={time}&size={size}&symbol={coin}";
                return http.DownloadString(url);
            }
            catch (WebException ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, $"---GetKLine {DateTime.Now}---");
                throw;
            }
        }
    }
}