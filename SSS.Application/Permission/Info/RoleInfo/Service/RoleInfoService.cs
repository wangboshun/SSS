using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Relation.RoleGroupRelation;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Repository.Permission.Info.RoleInfo;
using SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;
using SSS.Domain.Permission.Group.PowerGroup.Dto;

namespace SSS.Application.Permission.Info.RoleInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleInfoService))]
    public class RoleInfoService :
        QueryService<Domain.Permission.Info.RoleInfo.RoleInfo, RoleInfoInputDto, RoleInfoOutputDto>, IRoleInfoService
    {
        private readonly IRoleInfoRepository _roleInfoRepository;
        private readonly IRoleGroupRepository _roleGroupRepository;
        private readonly IRoleGroupRelationRepository _roleGroupRelationRepository;

        public RoleInfoService(IMapper mapper,
            IRoleInfoRepository repository,
            IErrorHandler error,
            IValidator<RoleInfoInputDto> validator,
            IRoleGroupRelationRepository roleGroupRelationRepository,
            IRoleGroupRepository roleGroupRepository) :
            base(mapper, repository, error, validator)
        {
            _roleInfoRepository = repository;
            _roleGroupRepository = roleGroupRepository;
            _roleGroupRelationRepository = roleGroupRelationRepository;
        }

        public void AddRoleInfo(RoleInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var role = Get(x => x.RoleName.Equals(input.rolename));
            if (role != null)
            {
                Error.Execute("角色名已存在！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.RoleInfo.RoleInfo>(input);
            model.CreateTime = DateTime.Now;

            if (!string.IsNullOrWhiteSpace(input.rolegroupid))
            {
                var rolegroup = _roleGroupRepository.Get(x => x.Id.Equals(input.rolegroupid));
                if (rolegroup != null)
                    _roleGroupRelationRepository.Add(new RoleGroupRelation()
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        RoleId = model.Id,
                        RoleGroupId = rolegroup.Id,
                        IsDelete = 0
                    });
            }

            Repository.Add(model);
            Repository.SaveChanges();
        }

        public void DeleteRoleInfo(RoleInfoInputDto input)
        {
            Repository.Remove(input.id, false);
            _roleGroupRelationRepository.Remove(x => x.RoleId.Equals(input.id));
            Repository.SaveChanges();
        }

        /// <summary>
        ///     获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleInfoTreeOutputDto> GetChildren(string roleid)
        {
            return _roleInfoRepository.GetChildren(roleid);
        }

        public Pages<List<RoleInfoOutputDto>> GetListRoleInfo(RoleInfoInputDto input)
        {
            return GetPage(input);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联角色
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<RoleInfoOutputDto>> GetRoleByRoleGroup(RoleGroupInputDto input)
        {
            var data = _roleInfoRepository.GetRoleByRoleGroup(input.id, input.rolegroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleInfoOutputDto>>(data.items.AsQueryable().ProjectTo<RoleInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        public Pages<List<RoleInfoOutputDto>> GetRoleByPowerGroup(PowerGroupInputDto input)
        {
            var data = _roleInfoRepository.GetRoleByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<RoleInfoOutputDto>>(data.items.AsQueryable().ProjectTo<RoleInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);

        }
    }
}