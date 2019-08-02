namespace SSS.Domain.CQRS.UserApi.Command.Commands
{
    public abstract class UserApiCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }

        public string ApiKey { set; get; }

        public string Secret { set; get; }

        public int Status { set; get; }

        public string PassPhrase { set; get; }

        public string UserId { set; get; }
    }
}
