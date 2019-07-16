using System;

namespace SSS.Domain.CQRS.UserConfig.Event.Events
{
    public class UserConfigAddEvent : Seedwork.Events.Event
    {
         public string id { set; get; } 

        public UserConfigAddEvent(Domain.UserConfig.UserConfig model)
        {
            this.id = model.Id; 
        }
    }
}
