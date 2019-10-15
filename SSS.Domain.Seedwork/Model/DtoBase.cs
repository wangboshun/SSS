namespace SSS.Domain.Seedwork.Model
{
    public abstract class InputDtoBase
    {
        public string id { get; set; }

        public int pageindex { set; get; }

        public int pagesize { set; get; }

        public string order_by { set; get; }

        public bool isdesc { set; get; }
    }

    public abstract class OutputDtoBase
    {
        public string id { get; set; }
    }
}