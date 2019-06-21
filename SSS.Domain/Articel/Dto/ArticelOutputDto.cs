using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel.Dto
{
    public class ArticelOutputDto : OutputDtoBase
    {
        public string title { set; get; }

        public string content { set; get; }

        public int contenttype { set; get; }

        public int sort { set; get; }

        public int ismain { set; get; }

        public string mainimage { set; get; }
    }
}
