using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

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
    public class JobInfoService : QueryService<SSS.Domain.System.Job.JobInfo.JobInfo, JobInfoInputDto, JobInfoOutputDto>, IJobInfoService
    {
        public JobInfoService(IMapper mapper,
            IJobInfoRepository repository,
            IErrorHandler error,
            IValidator<JobInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {

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
    }
}