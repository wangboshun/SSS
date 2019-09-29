namespace SSS.Domain.CQRS.UserActivity.Command.Commands
{
    public abstract class UserActivityCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }

        public string activityid { set; get; }

        public string userid { set; get; }

        public string wechatname { set; get; }

        public int grouptotal { set; get; }
    }
}
