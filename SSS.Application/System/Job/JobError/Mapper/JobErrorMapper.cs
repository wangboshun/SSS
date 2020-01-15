using AutoMapper;

using SSS.Domain.System.Job.JobError.Dto;

namespace SSS.Application.System.Job.JobError.Mapper
{
    public class JobErrorProfile : Profile
    {
        public JobErrorProfile()
        {
            CreateMap<Domain.System.Job.JobError.JobError, JobErrorOutputDto>();

            CreateMap<JobErrorInputDto, Domain.System.Job.JobError.JobError>();
        }
    }
}