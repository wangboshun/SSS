using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Community.CommunityBusinessRelation.Dto;
using SSS.Domain.Community.CommunityInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Community.CommunityBusiness;
using SSS.Infrastructure.Repository.Community.CommunityBusinessRelation;
using SSS.Infrastructure.Repository.Community.CommunityInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Community.CommunityInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(ICommunityInfoService))]
    public class CommunityInfoService : QueryService<SSS.Domain.Community.CommunityInfo.CommunityInfo, CommunityInfoInputDto, CommunityInfoOutputDto>, ICommunityInfoService
    {
        private readonly ICommunityBusinessRelationRepository _communityBusinessRelationRepository;
        private readonly ICommunityBusinessRepository _communityBusinessRepository;
        private readonly ICommunityInfoRepository _communityInfoRepository;

        public CommunityInfoService(IMapper mapper,
            ICommunityInfoRepository repository,
            IErrorHandler error,
            ICommunityBusinessRepository communityBusinessRepository,
            ICommunityInfoRepository communityInfoRepository,
        ICommunityBusinessRelationRepository communityBusinessRelationRepository,
            IValidator<CommunityInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _communityBusinessRepository = communityBusinessRepository;
            _communityInfoRepository = communityInfoRepository;
            _communityBusinessRelationRepository = communityBusinessRelationRepository;
        }

        public CommunityInfoOutputDto AddCommunityInfo(CommunityInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            var data = Repository.Have(input.id);
            if (data)
            {
                Error.Execute("社区已存在！");
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Community.CommunityInfo.CommunityInfo>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<CommunityInfoOutputDto>(model) : null;
        }

        public bool UpdateCommunityInfo(CommunityInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Update");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            var model = Mapper.Map<SSS.Domain.Community.CommunityInfo.CommunityInfo>(input);
            var data = Repository.Have(model.Id);
            if (!data)
            {
                Error.Execute("社区数据不存在！");
                return false;
            }

            model.UpdateTime = DateTime.Now;
            Repository.Update(model);
            return Repository.SaveChanges() > 0;
        }

        public CommunityBusinessRelationOutputDto AddCommunityBusinessRelation(CommunityBusinessRelationInputDto input)
        {
            var community = _communityInfoRepository.Get(input.CommunityId);
            var business = _communityBusinessRepository.Get(input.BusinessId);
            if (community == null || business == null)
            {
                Error.Execute("社区或业务不存在！");
                return null;
            }

            var relation = _communityBusinessRelationRepository.Get(x =>
                  x.Businessid.Equals(input.BusinessId) && x.Communityid.Equals(input.CommunityId));

            if (relation != null)
            {
                Error.Execute("社区业务已关联！");
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Community.CommunityBusinessRelation.CommunityBusinessRelation>(input);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _communityBusinessRelationRepository.Add(model);
            return _communityBusinessRelationRepository.SaveChanges() > 0 ? Mapper.Map<CommunityBusinessRelationOutputDto>(model) : null;
        }

        public Pages<List<CommunityInfoOutputDto>> GetListCommunityInfo(CommunityInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}