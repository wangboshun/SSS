namespace SSS.Domain.CQRS.Articel.Event.Events
{
    public class ArticelAddEvent : Seedwork.Events.Event
    {
        public string id { set; get; }

        public ArticelAddEvent(Domain.Articel.Articel model)
        {
            this.id = model.Id;
        }
    }
}
