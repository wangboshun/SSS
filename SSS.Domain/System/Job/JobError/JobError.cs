using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.System.Job.JobError
{
    public class JobError : Entity
    {


        public string Jobid { set; get; }

        public string Jobcount { set; get; }

        public string Message { set; get; }
    }
}