using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using Quartz;

using SSS.Application.Job;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobInfo.Dto;
using SSS.Infrastructure.Repository.System.Job.JobInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.System.Job.JobInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IJobInfoService))]
    public class JobInfoService : QueryService<SSS.Domain.System.Job.JobInfo.JobInfo, JobInfoInputDto, JobInfoOutputDto>, IJobInfoService
    {
        private readonly ISchedulerFactory _schedulerFactory;
        private readonly IJobInfoRepository _jobInfoRepository;
        private readonly IJobManager _jobManager;

        public JobInfoService(IMapper mapper,
            IJobInfoRepository repository,
            IErrorHandler error,
            ISchedulerFactory schedulerFactory,
            IJobInfoRepository jobInfoRepository,
            IJobManager jobManager,
            IValidator<JobInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _jobManager = jobManager;
            _schedulerFactory = schedulerFactory;
            _jobInfoRepository = jobInfoRepository;
        }

        public Pages<List<JobInfoOutputDto>> GetListJobInfo(JobInfoInputDto input)
        {
            return _jobInfoRepository.GetJobDetail(input.jobname, input.jobgroup, input.pageindex, input.pagesize);
        }

        /// <summary>
        /// 恢复Job
        /// </summary>
        /// <param name="input"></param>
        public bool ResumeJob(JobInfoInputDto input)
        {
            var job = _jobInfoRepository.Get(x => x.JobName.Equals(input.jobname) && x.JobGroup.Equals(input.jobgroup));
            if (job == null) return false;

            _jobManager.ResumeJob(input.jobname, input.jobgroup);
            job.JobStatus = (int)TriggerState.Normal;
            job.UpdateTime = DateTime.Now;
            _jobInfoRepository.Update(job);
            return _jobInfoRepository.SaveChanges() > 0;
        }

        /// <summary>
        /// 暂停Job
        /// </summary>
        /// <param name="input"></param>
        public bool PauseJob(JobInfoInputDto input)
        {
            var job = _jobInfoRepository.Get(x => x.JobName.Equals(input.jobname) && x.JobGroup.Equals(input.jobgroup));
            if (job == null) return false;

            _jobManager.PauseJob(input.jobname, input.jobgroup);
            var trigger_state = _jobManager.GeTriggerState(input.jobname, input.jobgroup);
            if (trigger_state != TriggerState.Paused) return false;

            job.JobStatus = (int)TriggerState.Paused;
            job.UpdateTime = DateTime.Now;
            _jobInfoRepository.Update(job);
            return _jobInfoRepository.SaveChanges() > 0;

        }

        /// <summary>
        /// 修改Job
        /// </summary>
        /// <param name="input"></param>
        public bool UpdateJob(JobInfoInputDto input)
        {
            var job = _jobInfoRepository.Get(x => x.JobName.Equals(input.jobname) && x.JobGroup.Equals(input.jobgroup));
            if (job == null) return false;

            _jobManager.UpdateJob(input.jobname, input.jobgroup, input.jobcron);
            job.JobCron = input.jobcron;
            job.UpdateTime = DateTime.Now;
            _jobInfoRepository.Update(job);
            return _jobInfoRepository.SaveChanges() > 0;
        }

        /// <summary>
        /// 删除Job
        /// </summary>
        /// <param name="input"></param>
        public bool DeleteJob(JobInfoInputDto input)
        {
            var job = _jobInfoRepository.Get(x => x.JobName.Equals(input.jobname) && x.JobGroup.Equals(input.jobgroup));
            if (job == null) return false;

            var result = _jobManager.DeleteJob(input.jobname, input.jobgroup).Result;
            var trigger_state = _jobManager.GeTriggerState(input.jobname, input.jobgroup);
            if (trigger_state != TriggerState.None) return false;
            job.IsDelete = 1;
            job.JobStatus = (int)TriggerState.None;
            job.UpdateTime = DateTime.Now;
            _jobInfoRepository.Update(job);
            return _jobInfoRepository.SaveChanges() > 0;
        }

        /// <summary>
        /// 添加Job
        /// </summary>
        /// <param name="input"></param>
        public bool AddJob(JobInfoInputDto input)
        {
            var job = _jobInfoRepository.Get(x => x.JobName.Equals(input.jobname) && x.JobGroup.Equals(input.jobgroup), true);
            if (job == null) return false;

            var result = _jobManager.AddJob(input.jobname, input.jobgroup, input.jobcron, job.JobValue, job.JobClass).Result;
            var trigger_state = _jobManager.GeTriggerState(input.jobname, input.jobgroup);
            if (trigger_state != TriggerState.Normal || trigger_state != TriggerState.Paused) return false;
            job.JobCron = input.jobcron;
            job.IsDelete = 0;
            job.JobStatus = (int)TriggerState.Normal;
            job.CreateTime = DateTime.Now;
            job.UpdateTime = DateTime.Now;
            _jobInfoRepository.Update(job);
            return _jobInfoRepository.SaveChanges() > 0;
        }

        /// <summary>
        /// 获取Job
        /// </summary>
        /// <param name="input"></param>
        public JobInfoOutputDto GetJob(JobInfoInputDto input)
        {
            var data = _jobInfoRepository.GetJobDetail(input.jobname, input.jobgroup, input.pageindex, input.pagesize);
            return data.items.FirstOrDefault();
        }
    }
}