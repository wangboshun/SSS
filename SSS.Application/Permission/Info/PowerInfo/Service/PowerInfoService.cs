using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.PowerInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Info.PowerInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerInfoService))]
    public class PowerInfoService :
        QueryService<Domain.Permission.Info.PowerInfo.PowerInfo, PowerInfoInputDto, PowerInfoOutputDto>,
        IPowerInfoService
    {
        private readonly IPowerInfoRepository _repository;
        private readonly IPowerGroupRepository _powerGroupRepository;
        private readonly IPowerGroupRelationRepository _powerGroupRelationRepository;

        public PowerInfoService(IMapper mapper,
            IPowerInfoRepository repository,
            IErrorHandler error,
            IValidator<PowerInfoInputDto> validator,
            IPowerGroupRelationRepository powerGroupRelationRepository,
            IPowerGroupRepository powerGroupRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _powerGroupRepository = powerGroupRepository;
            _powerGroupRelationRepository = powerGroupRelationRepository;
        }

        public void AddPowerInfo(PowerInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.PowerInfo.PowerInfo>(input);
            model.CreateTime = DateTime.Now;

            var group = _powerGroupRepository.Get(x => x.Id.Equals(input.powergroupid));
            _powerGroupRelationRepository.Add(new Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation()
            {
                CreateTime = DateTime.Now,
                Id = Guid.NewGuid().ToString(),
                PowerId = model.Id,
                PowerGroupId = group?.Id,
                IsDelete = 0
            });

            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerInfoOutputDto>> GetListPowerInfo(PowerInfoInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerInfo(PowerInfoInputDto input)
        {
            Repository.Remove(input.id, false);
            _powerGroupRelationRepository.Remove(x => x.PowerId.Equals(input.id));
            Repository.SaveChanges();
        }

        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<PowerInfoTreeOutputDto> GetChildren(string menuid)
        {
            return _repository.GetChildren(menuid);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerListByGroup(PowerGroupRelationInputDto input)
        {
            return _powerGroupRelationRepository.GetPowerListByGroup(input);
        }
    }
}