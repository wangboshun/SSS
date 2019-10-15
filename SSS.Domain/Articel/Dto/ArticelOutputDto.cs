using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel.Dto
{
    public class ArticelOutputDto : OutputDtoBase
    {
        public string title { set; get; }

        public int sort { set; get; }

        public string content { set; get; }

        public string author { set; get; }

        public string logo { set; get; }
    }
}