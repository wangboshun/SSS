using Quartz;

using SSS.Infrastructure.Util.Json;

using System;
using System.Text;

namespace SSS.Application.Job.Extension
{
    public static class JobEx
    {
        public static string GetJobDetail(this IJobExecutionContext context)
        {
            StringBuilder str = new StringBuilder();
            str.Append($"正在运行---------------> 任务：{context.JobDetail.Key} ");

            if (context.JobDetail.JobDataMap.Count > 0)
                str.Append($"任务传递参数：{ context.JobDetail.JobDataMap.ToJson() } ");

            str.Append($"任务时间：{DateTime.Now:yyyy-MM-dd HH:mm:ss:ffffff}");

            return str.ToString();
        }
    }
}
