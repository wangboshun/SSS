using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel
{
    public class Articel : Entity
    {
        public Articel(string id, string title, string content, int ContentType, string MainImage, int sort, int IsMain)
        {
            Id = id;
            Title = title;
            Content = content;
            this.MainImage = MainImage;
            this.ContentType = ContentType;
            Sort = sort;
            this.IsMain = IsMain;
        }

        public string Title { set; get; }

        public string Content { set; get; }

        public int ContentType { set; get; }

        public string MainImage { set; get; }

        public int Sort { set; get; }

        public int IsMain { set; get; }
    }
}