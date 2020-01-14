using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.System.Job.JobError.Dto;
using SSS.Infrastructure.Repository.System.Job.JobError;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.System.Job.JobError.Service
{
    [DIService(ServiceLifetime.Singleton, typeof(IJobErrorService))]
    public class JobErrorService : QueryService<SSS.Domain.System.Job.JobError.JobError, JobErrorInputDto, JobErrorOutputDto>, IJobErrorService
    {
        public JobErrorService(IMapper mapper,
            IJobErrorRepository repository,
            IErrorHandler error,
            IValidator<JobErrorInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public JobErrorOutputDto AddJobError(JobErrorInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.System.Job.JobError.JobError>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<JobErrorOutputDto>(model) : null;
        }

        public Pages<List<JobErrorOutputDto>> GetListJobError(JobErrorInputDto input)
        {
            return GetPage(input);
        }
    }
}