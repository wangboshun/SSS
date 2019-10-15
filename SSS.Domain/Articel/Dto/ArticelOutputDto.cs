using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel.Dto
{
    public class ArticelOutputDto : OutputDtoBase
    {
        public string Title { set; get; }

        public int Sort { set; get; }

        public string Content { set; get; }

        public string Author { set; get; }

        public string Logo { set; get; }
    }
}