using SSS.Domain.System.Job.JobInfo.Dto;

namespace SSS.Application.System.Job.JobInfo.Mapper
{
    public class JobInfoProfile : AutoMapper.Profile
    {
        public JobInfoProfile()
        {
            CreateMap<SSS.Domain.System.Job.JobInfo.JobInfo, JobInfoOutputDto>();

            CreateMap<JobInfoInputDto, SSS.Domain.System.Job.JobInfo.JobInfo>();
        }
    }
}
