using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.System.Job.JobError.Dto
{
    public class JobErrorOutputDto : OutputDtoBase
    {
        public string jobcount { set; get; }
        public string jobid { set; get; }
        public string message { set; get; }
    }
}