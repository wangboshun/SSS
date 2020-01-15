using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobInfo.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Ef;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.System.Job.JobInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IJobInfoRepository))]
    public class JobInfoRepository : Repository<Domain.System.Job.JobInfo.JobInfo>, IJobInfoRepository
    {
        public JobInfoRepository(SystemDbContext context) : base(context)
        {
        }

        public Pages<List<JobInfoOutputDto>> GetJobDetail(string job_name, string job_group, int pageindex, int pagesize)
        {
            var filed = "i.*,IF (e.ErrorCount != 0, e.ErrorCount,0) AS errorcount ";
            var sql =
                @"SELECT {0} FROM  JobInfo AS i  LEFT JOIN ( SELECT JobId,COUNT(*) AS errorcount FROM JobError GROUP BY JobId ) AS e ON i.Id = e.JobId   ";

            var where = $"  WHERE  i.JobName = '{job_name}'   AND i.JobGroup =  '{job_group}' ";
            if (!string.IsNullOrWhiteSpace(job_name) && !string.IsNullOrWhiteSpace(job_group))
                sql += where;

            var count = Db.Database.Count(string.Format(sql, " count(*) ", job_name, job_group));

            sql += " GROUP BY i.Id ";
            if (pageindex > 0 && pagesize > 0)
            {
                sql += $" limit {pageindex},{pagesize} ";
                var data = Db.Database.SqlQuery<JobInfoOutputDto>(string.Format(sql, filed)).ToList();
                return new Pages<List<JobInfoOutputDto>>(data, count);
            }

            {
                var data = Db.Database.SqlQuery<JobInfoOutputDto>(string.Format(sql, filed)).ToList();
                return new Pages<List<JobInfoOutputDto>>(data, count);
            }
        }
    }
}