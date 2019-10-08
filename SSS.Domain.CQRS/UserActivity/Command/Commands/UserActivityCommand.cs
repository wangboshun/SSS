using SSS.Domain.UserActivity.Dto;

namespace SSS.Domain.CQRS.UserActivity.Command.Commands
{
    public abstract class UserActivityCommand : SSS.Domain.Seedwork.Command.Command
    {
        public UserActivityInputDto inputDto { set; get; }
    }
}
