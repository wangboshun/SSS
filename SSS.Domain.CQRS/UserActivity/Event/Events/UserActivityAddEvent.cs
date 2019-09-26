using System;
using System.Collections.Generic;

namespace SSS.Domain.CQRS.UserActivity.Event.Events
{
    public class UserActivityAddEvent : Seedwork.Events.Event
    {
        public List<Domain.UserActivity.UserActivity> list { set; get; }

        public UserActivityAddEvent(List<Domain.UserActivity.UserActivity> list)
        {
            this.list = list;
        }
    }
}
