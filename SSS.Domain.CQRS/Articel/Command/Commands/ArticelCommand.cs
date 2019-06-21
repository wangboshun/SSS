namespace SSS.Domain.CQRS.Articel.Command.Commands
{
    public abstract class ArticelCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }

        public string title { set; get; }

        public string content { set; get; }

        public int contenttype { set; get; }

        public string mainimage { set; get; }

        public int sort { set; get; }

        public int issmain { set; get; }
    }
}
