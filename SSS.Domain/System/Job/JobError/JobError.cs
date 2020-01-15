using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.System.Job.JobError
{
    public class JobError : Entity
    {
        public int JobCount { set; get; }
        public string JobId { set; get; }
        public string Message { set; get; }
    }
}