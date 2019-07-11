namespace SSS.Domain.CQRS.UserInfo.Command.Commands
{
    public abstract class UserInfoCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }

        public string code { set; get; }

        public string phone { set; get; }

        public string uid { set; get; }

        public string password { set; get; }

        public string firstid { set; get; }

        public string secondid { set; get; }

        public string thirdid { set; get; }
    }
}
