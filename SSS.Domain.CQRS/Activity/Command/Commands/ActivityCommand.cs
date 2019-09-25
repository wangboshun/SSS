using System;

namespace SSS.Domain.CQRS.Activity.Command.Commands
{
    public abstract class ActivityCommand : SSS.Domain.Seedwork.Command.Command
    {
         public string id { set; get; }
    }
}
