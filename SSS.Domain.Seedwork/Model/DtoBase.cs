using System;

namespace SSS.Domain.Seedwork.Model
{
    public abstract class InputDtoBase
    {
        public string id { get; set; }

        public bool isdesc { set; get; }
        public string order_by { set; get; }
        public int pageindex { set; get; }

        public int pagesize { set; get; }
    }

    public abstract class OutputDtoBase
    {
        public DateTime createtime { set; get; }
        public string id { get; set; }
    }
}