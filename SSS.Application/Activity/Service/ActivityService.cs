using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Activity.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Activity;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Activity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IActivityService))]
    public class ActivityService : QueryService<SSS.Domain.Activity.Activity, ActivityInputDto, ActivityOutputDto>, IActivityService
    {
        public ActivityService(IMapper mapper,
            IActivityRepository repository,
            IErrorHandler error, IValidator<ActivityInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddActivity(ActivityInputDto input)
        {
            var result = _validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                _error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = _mapper.Map<SSS.Domain.Activity.Activity>(input);
            _repository.Add(model);
            _repository.SaveChanges();
        }

        public Pages<List<ActivityOutputDto>> GetListActivity(ActivityInputDto input)
        {
            return GetList(input);
        }

        public ActivityOutputDto GetById(ActivityInputDto input)
        {
            return _mapper.Map<ActivityOutputDto>(Get(x => x.Id.Equals(input.id) && x.IsDelete == 0));
        }
    }
}