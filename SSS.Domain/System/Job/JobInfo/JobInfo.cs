using SSS.Domain.Seedwork.Model;

using System;

namespace SSS.Domain.System.Job.JobInfo
{
    public class JobInfo : Entity
    {
        public string JobName { set; get; }

        public string JobGroup { set; get; }

        public string JobValue { set; get; }
        public string JobCron { set; get; }
        public string JobResult { set; get; }
        public string JobClass { set; get; }
        public int JobCount { set; get; }
        public int JobStatus { set; get; }

        public DateTime? JobStartTime { set; get; }

        public DateTime? JobNextTime { set; get; }
    }
}