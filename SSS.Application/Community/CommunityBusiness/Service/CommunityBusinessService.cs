using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Community.CommunityBusiness.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Community.CommunityBusiness;
using SSS.Infrastructure.Repository.Community.CommunityBusinessRelation;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Community.CommunityBusiness.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICommunityBusinessService))]
    public class CommunityBusinessService : QueryService<SSS.Domain.Community.CommunityBusiness.CommunityBusiness, CommunityBusinessInputDto, CommunityBusinessOutputDto>, ICommunityBusinessService
    {
        private readonly ICommunityBusinessRelationRepository _communityBusinessRelationRepository;

        public CommunityBusinessService(IMapper mapper,
            ICommunityBusinessRepository repository,
            IErrorHandler error,
            ICommunityBusinessRelationRepository communityBusinessRelationRepository,
            IValidator<CommunityBusinessInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _communityBusinessRelationRepository = communityBusinessRelationRepository;
        }

        public CommunityBusinessOutputDto AddCommunityBusiness(CommunityBusinessInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            var data = Repository.Get(input.id);
            if (data != null)
            {
                Error.Execute("业务已存在！");
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Community.CommunityBusiness.CommunityBusiness>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CommunityBusinessOutputDto>(model) : null;
        }

        public Pages<List<CommunityBusinessOutputDto>> GetCommunityBusinessByCommunity(CommunityInfoInputDto input)
        {
            var data = _communityBusinessRelationRepository.GetListCommunityBusinessRelation(input.id, input.name);

            return new Pages<List<CommunityBusinessOutputDto>>(
                data.items.MapperToOutPut<CommunityBusinessOutputDto>()?.ToList(), data.count);
        }

        public Pages<List<CommunityBusinessOutputDto>> GetListCommunityBusiness(CommunityBusinessInputDto input)
        {
            return GetPage(input);
        }

        public bool UpdateCommunityBusiness(CommunityBusinessInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Update");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            var model = Mapper.Map<Domain.Community.CommunityBusiness.CommunityBusiness>(input);
            var data = Repository.Have(model.Id);
            if (!data)
            {
                Error.Execute("业务数据不存在！");
                return false;
            }

            model.UpdateTime = DateTime.Now;
            Repository.Update(model);
            return Repository.SaveChanges() > 0;
        }
    }
}