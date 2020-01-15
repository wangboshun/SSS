using AutoMapper;

using SSS.Domain.System.Job.JobInfo.Dto;

namespace SSS.Application.System.Job.JobInfo.Mapper
{
    public class JobInfoProfile : Profile
    {
        public JobInfoProfile()
        {
            CreateMap<Domain.System.Job.JobInfo.JobInfo, JobInfoOutputDto>();

            CreateMap<JobInfoInputDto, Domain.System.Job.JobInfo.JobInfo>();
        }
    }
}