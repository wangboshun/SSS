using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.PowerInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;
using SSS.Domain.Permission.Group.RoleGroup.Dto;

namespace SSS.Application.Permission.Info.PowerInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerInfoService))]
    public class PowerInfoService :
        QueryService<Domain.Permission.Info.PowerInfo.PowerInfo, PowerInfoInputDto, PowerInfoOutputDto>,
        IPowerInfoService
    {
        private readonly IPowerInfoRepository _powerInfoRepository;
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
            _powerInfoRepository = repository;
            _powerGroupRepository = powerGroupRepository;
            _powerGroupRelationRepository = powerGroupRelationRepository;
        }

        public bool AddPowerInfo(PowerInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.PowerInfo.PowerInfo>(input);
            model.CreateTime = DateTime.Now;

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                var powergroup = _powerGroupRepository.Get(x => x.Id.Equals(input.powergroupid));
                if (powergroup != null)
                    _powerGroupRelationRepository.Add(new Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation()
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        PowerId = model.Id,
                        PowerGroupId = powergroup.Id,
                        IsDelete = 0
                    });
            }

            Repository.Add(model);
            return Repository.SaveChanges()>0;
        }

        public Pages<List<PowerInfoOutputDto>> GetListPowerInfo(PowerInfoInputDto input)
        {
            return GetPage(input);
        }

        public bool DeletePowerInfo(string id)
        {
            Repository.Remove(id, false);
            _powerGroupRelationRepository.Remove(x => x.PowerId.Equals(id));
            return Repository.SaveChanges()>0;
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerInfoOutputDto>> GetPowerByRoleGroup(RoleGroupInputDto input)
        {
            var data = _powerInfoRepository.GetPowerByRoleGroup(input.id, input.rolegroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerInfoOutputDto>>(data.items.AsQueryable().ProjectTo<PowerInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<PowerInfoTreeOutputDto> GetChildren(string menuid)
        {
            return _powerInfoRepository.GetChildren(menuid);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerInfoOutputDto>> GetPowerByPowerGroup(PowerGroupInputDto input)
        {
            var data = _powerInfoRepository.GetPowerByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerInfoOutputDto>>(data.items.AsQueryable().ProjectTo<PowerInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }
    }
}