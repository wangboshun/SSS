namespace SSS.Domain.CQRS.UserInfo.Command.Commands
{
    public abstract class UserInfoCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }

        public string name { set; get; }

        public string phone { set; get; }

        public string openid { set; get; }

        public string code { set; get; }
    }
}
