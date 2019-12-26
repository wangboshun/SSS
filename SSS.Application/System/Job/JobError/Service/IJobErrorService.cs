using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobError.Dto;

using System.Collections.Generic;

namespace SSS.Application.System.Job.JobError.Service
{
    public interface IJobErrorService : IQueryService<SSS.Domain.System.Job.JobError.JobError, JobErrorInputDto, JobErrorOutputDto>
    {
        JobErrorOutputDto AddJobError(JobErrorInputDto input);

        Pages<List<JobErrorOutputDto>> GetListJobError(JobErrorInputDto input);
    }
}