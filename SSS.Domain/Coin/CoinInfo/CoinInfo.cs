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
        ///     ���ִ��루Ψһ������
        /// </summary>
        public string id { get; set; }

        /// <summary>
        ///     ����Ӣ������
        /// </summary>
        public string name { get; set; }

        /// <summary>
        ///     ���ֵļ��
        /// </summary>
        public string symbol { get; set; }

        /// <summary>
        ///     ���ֵ�����
        /// </summary>
        public string rank { get; set; }

        /// <summary>
        ///     ���ֵ�logo��webp��ʽ��
        /// </summary>
        public string logo { get; set; }

        /// <summary>
        ///     ���ֵ�logo����webp��ʽ��
        /// </summary>
        public string logo_png { get; set; }

        /// <summary>
        ///     ���¼۸񣨵�λ����Ԫ��
        /// </summary>
        public string price_usd { get; set; }

        /// <summary>
        ///     ���¼۸񣨵�λ��BTC��
        /// </summary>
        public string price_btc { get; set; }

        /// <summary>
        ///     24h�ĳɽ����λ����Ԫ��
        /// </summary>
        public string volume_24h_usd { get; set; }

        /// <summary>
        ///     ��ͨ��ֵ����λ����Ԫ��
        /// </summary>
        public string market_cap_usd { get; set; }

        /// <summary>
        ///     ��ͨ����
        /// </summary>
        public string available_supply { get; set; }

        /// <summary>
        ///     �ܷ�����
        /// </summary>
        public string total_supply { get; set; }

        /// <summary>
        ///     ��������������������>�ܷ�������Ʃ����Щ���ֻ���������һ����������
        /// </summary>
        public string max_supply { get; set; }

        /// <summary>
        ///     1Сʱ�ǵ���
        /// </summary>
        public string percent_change_1h { get; set; }

        /// <summary>
        ///     24Сʱ�ǵ���
        /// </summary>
        public string percent_change_24h { get; set; }

        /// <summary>
        ///     7���ǵ���
        /// </summary>
        public string percent_change_7d { get; set; }

        /// <summary>
        ///     �������ʱ�䣨10λunixʱ�����
        /// </summary>
        public string last_updated { get; set; }
    }
}