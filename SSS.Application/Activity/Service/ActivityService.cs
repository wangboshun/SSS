using System;
using System.Collections.Generic;
using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Activity.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Activity;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Application.Activity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IActivityService))]
    public class ActivityService : QueryService<Domain.Activity.Activity, ActivityInputDto, ActivityOutputDto>,
        IActivityService
    {
        public ActivityService(IMapper mapper,
            IActivityRepository repository,
            IErrorHandler error, IValidator<ActivityInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddActivity(ActivityInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Activity.Activity>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<ActivityOutputDto>> GetListActivity(ActivityInputDto input)
        {
            return GetPage(input);
        }

        public ActivityOutputDto GetById(ActivityInputDto input)
        {
            return Mapper.Map<ActivityOutputDto>(Get(x => x.Id.Equals(input.id) && x.IsDelete == 0));
        }
    }
}