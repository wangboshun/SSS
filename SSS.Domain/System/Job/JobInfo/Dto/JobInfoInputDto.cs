using SSS.Domain.Seedwork.Model; 

namespace SSS.Domain.System.Job.JobInfo.Dto
{
    public class JobInfoInputDto : InputDtoBase
    { 
        public string jobname { set; get; }
        public string jobgroup { set; get; }
    }
}
