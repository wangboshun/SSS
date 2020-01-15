using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobInfo.Dto;

using System.Collections.Generic;

namespace SSS.Application.System.Job.JobInfo.Service
{
    public interface IJobInfoService : IQueryService<SSS.Domain.System.Job.JobInfo.JobInfo, JobInfoInputDto, JobInfoOutputDto>
    {
        bool AddJob(JobInfoInputDto input);

        bool DeleteJob(JobInfoInputDto input);

        JobInfoOutputDto GetJob(JobInfoInputDto input);

        Pages<List<JobInfoOutputDto>> GetListJobInfo(JobInfoInputDto input);

        bool PauseJob(JobInfoInputDto input);

        bool ResumeJob(JobInfoInputDto input);

        bool UpdateJob(JobInfoInputDto input);
    }
}