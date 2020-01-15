using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.System.Job.JobInfo.Dto
{
    public class JobInfoInputDto : InputDtoBase
    {
        public string jobcron { set; get; }
        public string jobgroup { set; get; }
        public string jobname { set; get; }
    }
}