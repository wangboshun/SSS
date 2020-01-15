using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinInfo
{
    public class CoinInfo : Entity
    {
        public string Coin { set; get; }

        public string Name { set; get; }

        public string RomteLogo { set; get; }

        public string LocalLogo { set; get; }

        public string Imagedata { set; get; }

        public string Content { set; get; }
    }

    public class CoinJson
    {
        /// <summary>
        ///     币种代码（唯一主键）
        /// </summary>
        public string id { get; set; }

        /// <summary>
        ///     币种英文名称
        /// </summary>
        public string name { get; set; }

        /// <summary>
        ///     币种的简称
        /// </summary>
        public string symbol { get; set; }

        /// <summary>
        ///     币种的排名
        /// </summary>
        public string rank { get; set; }

        /// <summary>
        ///     币种的logo（webp格式）
        /// </summary>
        public string logo { get; set; }

        /// <summary>
        ///     币种的logo（非webp格式）
        /// </summary>
        public string logo_png { get; set; }

        /// <summary>
        ///     最新价格（单位：美元）
        /// </summary>
        public string price_usd { get; set; }

        /// <summary>
        ///     最新价格（单位：BTC）
        /// </summary>
        public string price_btc { get; set; }

        /// <summary>
        ///     24h的成交额（单位：美元）
        /// </summary>
        public string volume_24h_usd { get; set; }

        /// <summary>
        ///     流通市值（单位：美元）
        /// </summary>
        public string market_cap_usd { get; set; }

        /// <summary>
        ///     流通数量
        /// </summary>
        public string available_supply { get; set; }

        /// <summary>
        ///     总发行量
        /// </summary>
        public string total_supply { get; set; }

        /// <summary>
        ///     最大发行量（最大发行量可能>总发行量，譬如有些币种会主动销毁一部分数量）
        /// </summary>
        public string max_supply { get; set; }

        /// <summary>
        ///     1小时涨跌幅
        /// </summary>
        public string percent_change_1h { get; set; }

        /// <summary>
        ///     24小时涨跌幅
        /// </summary>
        public string percent_change_24h { get; set; }

        /// <summary>
        ///     7天涨跌幅
        /// </summary>
        public string percent_change_7d { get; set; }

        /// <summary>
        ///     行情更新时间（10位unix时间戳）
        /// </summary>
        public string last_updated { get; set; }
    }
}