using Quartz;

using System.Threading.Tasks;

namespace SSS.Application.Job.JobSetting.Manager
{
    public interface IJobManager
    {
        Task<bool> AddJob(string job_name, string job_group, string job_cron, string job_value, string job_class_str);

        Task<bool> DeleteJob(string job_name, string job_group);

        IJobDetail GetJobDetail(string job_name, string job_group);

        TriggerState GeTriggerState(string job_name, string job_group);

        ITrigger GetTrigger(string job_name, string job_group);

        Task PauseJob(string job_name, string job_group);

        Task ResumeJob(string job_name, string job_group);

        Task<string> Start();

        void Stop();

        Task UpdateJob(string job_name, string job_group, string job_cron);
    }
}