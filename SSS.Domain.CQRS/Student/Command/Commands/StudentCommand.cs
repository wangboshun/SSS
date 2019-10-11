namespace SSS.Domain.CQRS.Student.Command.Commands
{
    public abstract class StudentCommand : SSS.Domain.Seedwork.Command.Command
    {
        public string id { set; get; }
        public string name { get; set; }
        public int age { set; get; }
    }
}
