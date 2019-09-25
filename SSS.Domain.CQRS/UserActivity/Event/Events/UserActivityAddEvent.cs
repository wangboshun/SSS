using System;

namespace SSS.Domain.CQRS.UserActivity.Event.Events
{
    public class UserActivityAddEvent : Seedwork.Events.Event
    {
         public string id { set; get; } 

        public UserActivityAddEvent(Domain.UserActivity.UserActivity model)
        {
            this.id = model.Id; 
        }
    }
}
