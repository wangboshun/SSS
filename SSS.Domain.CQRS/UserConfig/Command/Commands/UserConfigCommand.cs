namespace SSS.Domain.CQRS.UserConfig.Command.Commands
{
    public abstract class UserConfigCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }
        public string coin { set; get; }
        public int ktime { set; get; }
        public int profit { set; get; }
        public int loss { set; get; }
        public string userid { set; get; }
        public double size { set; get; }
    }
}
