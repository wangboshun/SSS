using Microsoft.Extensions.Logging;

using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

using SSS.DigitalCurrency.Domain;
using SSS.Infrastructure.Util.DateTime;

using System;
using System.Collections.Generic;
using System.Net;
using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;

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
            List<CoinSymbols> list = new List<CoinSymbols>();

            try
            {
                WebClient http = new WebClient();

                string result = http.DownloadString("https://api.huobiasia.vip/v1/common/symbols");

                JObject json_root = (JObject)JsonConvert.DeserializeObject(result);

                var json_data = json_root["data"];
                foreach (var item in json_data)
                    if (item["quote-currency"].ToString().Contains("usdt"))
                    {
                        CoinSymbols s = new CoinSymbols();

                        s.base_currency = item["base-currency"].ToString();
                        s.quote_currency = item["quote-currency"].ToString();
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
        public List<KLine> GetKLine(string coin,string quote, string time, int size)
        {
            List<KLine> list = new List<KLine>();
            try
            {
                WebClient http = new WebClient();

                string result =
                    http.DownloadString($"https://api.huobiasia.vip/market/history/kline?period={time}&size={size}&symbol={coin+quote}");

                JObject json_root = (JObject)JsonConvert.DeserializeObject(result);

                if (json_root.GetValue("status").ToString().Equals("error"))
                    return null;

                list = JsonConvert.DeserializeObject<List<KLine>>(json_root["data"].ToString());

                foreach (var item in list) item.time = DateTimeConvert.ConvertIntDateTime(item.id);

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
