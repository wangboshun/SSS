using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Community.CommunityTask.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Community.CommunityBusiness;
using SSS.Infrastructure.Repository.Community.CommunityTask;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Community.CommunityTask.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICommunityTaskService))]
    public class CommunityTaskService : QueryService<SSS.Domain.Community.CommunityTask.CommunityTask, CommunityTaskInputDto, CommunityTaskOutputDto>, ICommunityTaskService
    {
        private readonly ICommunityBusinessRepository _communityBusinessRepository;

        public CommunityTaskService(IMapper mapper,
            ICommunityTaskRepository repository,
            IErrorHandler error,
            IValidator<CommunityTaskInputDto> validator,
            ICommunityBusinessRepository communityBusinessRepository) :
            base(mapper, repository, error, validator)
        {
            _communityBusinessRepository = communityBusinessRepository;
        }

        public CommunityTaskOutputDto AddCommunityTask(CommunityTaskInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            var business = _communityBusinessRepository.Get(input.businessid);
            if (business == null)
            {
                Error.Execute("业务不存在！");
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Community.CommunityTask.CommunityTask>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CommunityTaskOutputDto>(model) : null;
        }

        public Pages<List<CommunityTaskOutputDto>> GetListCommunityTask(CommunityTaskInputDto input)
        {
            return GetPage(input);
        }
    }
}