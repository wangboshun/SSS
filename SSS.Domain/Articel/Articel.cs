using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel
{
    public class Articel : Entity
    {
        public Articel(string id)
        {
            Id = id;
        }


        public string Title { set; get; }

        public int Sort { set; get; }

        public string Content { set; get; }

        public string Author { set; get; }

        public string Logo { set; get; }
    }

    public class JinSe_News
    {
        public string id { set; get; }

        public string content { set; get; }

        public string title { set; get; }

        public string createtime { set; get; }
    }
}