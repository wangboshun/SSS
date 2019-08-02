namespace SSS.Domain.CQRS.UserApi.Event.Events
{
    public class UserApiUpdateEvent : Seedwork.Events.Event
    {
        public string id { set; get; }

        public UserApiUpdateEvent(Domain.UserApi.UserApi model)
        {
            this.id = model.Id;
        }
    }
}
