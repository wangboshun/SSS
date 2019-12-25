using System.Threading.Tasks;

namespace SSS.Application.Seedwork.Job
{
    public interface IJobManager
    {
        Task ResumeJob(string jobname, string jobgroup);
        Task PauseJob(string jobname, string jobgroup);
        Task<bool> DeleteJob(string jobname, string jobgroup);
        Task<string> Start();
    }
}