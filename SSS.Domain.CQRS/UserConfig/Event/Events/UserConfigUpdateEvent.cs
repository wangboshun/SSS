namespace SSS.Domain.CQRS.UserConfig.Event.Events
{
    public class UserConfigUpdateEvent : Seedwork.Events.Event
    {
        public string id { set; get; }

        public UserConfigUpdateEvent(Domain.UserConfig.UserConfig model)
        {
            this.id = model.Id;
        }
    }
}
