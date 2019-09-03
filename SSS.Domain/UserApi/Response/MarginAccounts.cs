namespace SSS.Domain.UserApi.Response
{
    public class MarginAccounts
    {
        public string instrument_id { set; get; }

        public string balance { set; get; }

        public string hold { set; get; }

        public string available { set; get; }

        public string risk_rate { set; get; }

        public string can_withdraw { set; get; }

        public string liquidation_price { set; get; }

        public string borrowed { set; get; }

        public string lending_fee { set; get; }

        public string margin_ratio { set; get; }
    }
}
