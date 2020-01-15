using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Domain.System.Job.JobInfo.Dto;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.System.Job.JobInfo
{
    public interface IJobInfoRepository : IRepository<Domain.System.Job.JobInfo.JobInfo>
    {
        Pages<List<JobInfoOutputDto>> GetJobDetail(string job_name, string job_group, int pageindex, int pagesize);
    }
}