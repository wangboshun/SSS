using SSS.Domain.Seedwork.Model;

using System;

namespace SSS.Domain.System.Job.JobInfo.Dto
{
    public class JobInfoOutputDto : OutputDtoBase
    {
        public string jobname { set; get; }
        public string jobgroup { set; get; }
        public string jobcron { set; get; }
        public int jobcount { set; get; }
        public DateTime jobnexttime { set; get; }
        public DateTime jobstarttime { set; get; }
        public int jobstatus { set; get; }
        public string jobclass { set; get; }
        public string jobresult { set; get; }
        public string updatetime { set; get; }
        public int errorcount { set; get; }
    }
}
