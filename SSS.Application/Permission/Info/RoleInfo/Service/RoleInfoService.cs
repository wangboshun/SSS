using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Domain.Permission.RoleInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Info.RoleInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using SSS.Infrastructure.Repository.Permission.Relation.RoleRoleGroupRelation;

namespace SSS.Application.Permission.Info.RoleInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleInfoService))]
    public class RoleInfoService :
        QueryService<Domain.Permission.Info.RoleInfo.RoleInfo, RoleInfoInputDto, RoleInfoOutputDto>, IRoleInfoService
    {
        private readonly IRoleInfoRepository _repository;

        private readonly IRoleUserGroupRelationRepository _roleRoleGroupRelationRepository;

        public RoleInfoService(IMapper mapper,
            IRoleInfoRepository repository,
            IErrorHandler error,
            IValidator<RoleInfoInputDto> validator,
            IRoleUserGroupRelationRepository roleRoleGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _roleRoleGroupRelationRepository = roleRoleGroupRelationRepository;
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
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public void DeleteRoleInfo(RoleInfoInputDto input)
        {
            Delete(input.id);
        }

        /// <summary>
        ///     获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleInfoTreeOutputDto> GetChildren(string roleid)
        {
            return _repository.GetChildren(roleid);
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
        public Pages<List<RoleRoleGroupRelationOutputDto>> GetRoleListGroupByGroup(RoleRoleGroupRelationInputDto input)
        {
            return _roleRoleGroupRelationRepository.GetRoleListGroupByGroup(input);
        }
    }
}