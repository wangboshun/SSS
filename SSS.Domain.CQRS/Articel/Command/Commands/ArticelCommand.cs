namespace SSS.Domain.CQRS.Articel.Command.Commands
{
    public abstract class ArticelCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }
    }
}
