using SSS.Domain.UserInfo.Dto;

namespace SSS.Domain.CQRS.UserInfo.Command.Commands
{
    public abstract class UserInfoCommand : SSS.Domain.Seedwork.Command.Command
    {
        public UserInfoInputDto inputDto { set; get; }
    }
}
