using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Coin.CoinAnalyse.Dto
{
    public class CoinAnalyseOutputDto : OutputDtoBase
    {
        /// <summary>
        ///     ���׶�
        /// </summary>
        public string Coin { set; get; }

        /// <summary>
        ///     ������
        /// </summary>
        public string Platform { set; get; }

        /// <summary>
        ///     ʱ���
        /// </summary>
        public string TimeType { set; get; }

        /// <summary>
        ///     ���̼�
        /// </summary>
        public double Open { set; get; }

        /// <summary>
        ///     ���̼�
        /// </summary>
        public double Close { set; get; }

        /// <summary>
        ///     ��߼�
        /// </summary>
        public double High { set; get; }

        /// <summary>
        ///     ��ͼ�
        /// </summary>
        public double Low { set; get; }

        /// <summary>
        ///     ����
        /// </summary>
        public string Desc { set; get; }

        /// <summary>
        ///     �ǵ���
        /// </summary>
        public double HighRange { set; get; }

        /// <summary>
        ///     ��ǰ�Ƿ�
        /// </summary>
        public double CloseRange { set; get; }

        /// <summary>
        ///     Logo
        /// </summary>
        public string Logo { set; get; }
    }
}