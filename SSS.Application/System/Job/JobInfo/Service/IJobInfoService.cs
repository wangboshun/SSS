using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobInfo.Dto;

using System.Collections.Generic;

namespace SSS.Application.System.Job.JobInfo.Service
{
    public interface IJobInfoService : IQueryService<SSS.Domain.System.Job.JobInfo.JobInfo, JobInfoInputDto, JobInfoOutputDto>
    {
        JobInfoOutputDto AddJobInfo(JobInfoInputDto input);

        Pages<List<JobInfoOutputDto>> GetListJobInfo(JobInfoInputDto input);
    }
}