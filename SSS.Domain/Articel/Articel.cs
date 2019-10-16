using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel
{
    public class Articel : Entity
    {
        public string Title { set; get; }

        public int Sort { set; get; }

        public string Content { set; get; }

        public string Author { set; get; }

        public string Logo { set; get; }

        public int Category { set; get; }
    }
}