using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Articel
{
    public class Articel : Entity
    {
        public Articel(string id)
        {
            this.Id = id;
        }

        public string Title { set; get; }

        public string Content { set; get; }

        public int Sort { set; get; }

        public int IsMain { set; get; }      
    }
}