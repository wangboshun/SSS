namespace SSS.Domain.CQRS.Student.Event.Events
{
    public class StudentUpdateEvent : Seedwork.Events.Event
    {
        public string id { set; get; }

        public string name { set; get; }

        public int age { set; get; }

        public StudentUpdateEvent(Domain.Student.Student student)
        {
            this.id = student.Id;
            this.name = student.Name;
            this.age = student.Age;
        }
    }
}
