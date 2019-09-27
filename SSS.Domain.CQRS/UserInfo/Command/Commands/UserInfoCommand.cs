using System;

namespace SSS.Domain.CQRS.UserInfo.Command.Commands
{
    public abstract class UserInfoCommand : SSS.Domain.Seedwork.Command.Command
    {
         public string id { set; get; }

         public string username { set; get; }

         public string password { set; get; }
    }
}
