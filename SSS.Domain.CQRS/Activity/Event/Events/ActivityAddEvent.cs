using System;

namespace SSS.Domain.CQRS.Activity.Event.Events
{
    public class ActivityAddEvent : Seedwork.Events.Event
    {
         public string id { set; get; } 

        public ActivityAddEvent(Domain.Activity.Activity model)
        {
            this.id = model.Id; 
        }
    }
}
