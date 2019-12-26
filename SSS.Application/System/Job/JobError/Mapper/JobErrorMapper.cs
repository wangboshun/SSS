using SSS.Domain.System.Job.JobError.Dto;

namespace SSS.Application.System.Job.JobError.Mapper
{
    public class JobErrorProfile : AutoMapper.Profile
    {
        public JobErrorProfile()
        {
            CreateMap<SSS.Domain.System.Job.JobError.JobError, JobErrorOutputDto>();

            CreateMap<JobErrorInputDto, SSS.Domain.System.Job.JobError.JobError>();
        }
    }
}
