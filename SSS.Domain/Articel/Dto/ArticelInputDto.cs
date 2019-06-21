using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel.Dto
{
    public class ArticelInputDto : InputDtoBase
    {
        public string title { set; get; }

        public string content { set; get; }

        public int? contenttype { set; get; }

        public string mainimage { set; get; }

        public int? sort { set; get; }

        public int? issmain { set; get; }
    }
}
