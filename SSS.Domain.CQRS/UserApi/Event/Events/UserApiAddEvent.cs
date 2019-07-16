using System;

namespace SSS.Domain.CQRS.UserApi.Event.Events
{
    public class UserApiAddEvent : Seedwork.Events.Event
    {
         public string id { set; get; } 

        public UserApiAddEvent(Domain.UserApi.UserApi model)
        {
            this.id = model.Id; 
        }
    }
}
