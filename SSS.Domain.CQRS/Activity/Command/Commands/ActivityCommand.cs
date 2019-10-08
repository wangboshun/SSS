using SSS.Domain.Activity.Dto;

namespace SSS.Domain.CQRS.Activity.Command.Commands
{
    public abstract class ActivityCommand : SSS.Domain.Seedwork.Command.Command
    {
        public ActivityInputDto inputDto { set; get; }
    }
}
