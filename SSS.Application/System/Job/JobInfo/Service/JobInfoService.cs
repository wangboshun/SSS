using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using Quartz;

using SSS.Application.Seedwork.Job;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobInfo.Dto;
using SSS.Infrastructure.Repository.System.Job.JobInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.System.Job.JobInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IJobInfoService))]
    public class JobInfoService :
        QueryService<SSS.Domain.System.Job.JobInfo.JobInfo, JobInfoInputDto, JobInfoOutputDto>, IJobInfoService
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

        public JobInfoOutputDto AddJobInfo(JobInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.System.Job.JobInfo.JobInfo>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<JobInfoOutputDto>(model) : null;
        }

        public Pages<List<JobInfoOutputDto>> GetListJobInfo(JobInfoInputDto input)
        {
            return GetPage(input);
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
            job.JobStatus = 1;
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
            job.JobStatus = 0;
            _jobInfoRepository.Update(job);
            return _jobInfoRepository.SaveChanges() > 0;
        }
    }
}