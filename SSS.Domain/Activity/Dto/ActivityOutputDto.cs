using System;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Activity.Dto
{
    public class ActivityOutputDto : OutputDtoBase
    {
        public string title { set; get; }

        public int grouptotal { set; get; }

        public string qrcode { set; get; }

        public string content { set; get; }

        public int maxjoin { set; get; }

        public DateTime starttime { set; get; }

        public DateTime endtime { set; get; }
    }
}