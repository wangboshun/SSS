using System;

namespace SSS.Domain.CQRS.UserActivity.Command.Commands
{
    public abstract class UserActivityCommand : SSS.Domain.Seedwork.Command.Command
    {
         public string id { set; get; }
    }
}
