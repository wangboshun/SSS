using System;

namespace SSS.Domain.CQRS.UserConfig.Command.Commands
{
    public abstract class UserConfigCommand : SSS.Domain.Seedwork.Command.Command
    {
         public string id { set; get; }
    }
}
