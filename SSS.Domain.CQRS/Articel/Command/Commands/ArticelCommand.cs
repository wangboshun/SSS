using SSS.Domain.Articel.Dto;

namespace SSS.Domain.CQRS.Articel.Command.Commands
{
    public abstract class ArticelCommand : SSS.Domain.Seedwork.Command.Command
    {
        public ArticelInputDto inputDto { set; get; }
    }
}
