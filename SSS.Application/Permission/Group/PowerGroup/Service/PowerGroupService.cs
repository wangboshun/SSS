using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;
using SSS.Domain.Permission.Group.RoleGroup.Dto;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupService))]
    public class PowerGroupService :
        QueryService<Domain.Permission.Group.PowerGroup.PowerGroup, PowerGroupInputDto, PowerGroupOutputDto>,
        IPowerGroupService
    {
        private readonly IPowerGroupRepository _powerGroupRepository;

        public PowerGroupService(IMapper mapper,
            IPowerGroupRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _powerGroupRepository = repository;
        }

        public bool AddPowerGroup(PowerGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.PowerGroup.PowerGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            return Repository.SaveChanges() > 0;
        }

        public Pages<List<PowerGroupOutputDto>> GetListPowerGroup(PowerGroupInputDto input)
        {
            return GetPage(input);
        }

        public bool DeletePowerGroup(string id)
        {
            return Repository.Remove(id);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOutputDto>> GetPowerGroupByPower(PowerInfoInputDto input)
        {
            var data = _powerGroupRepository.GetPowerGroupByPower(input.id, input.powername, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerGroupOutputDto>>(data.items?.AsQueryable().ProjectTo<PowerGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOutputDto>> GetPowerGroupByMenu(MenuInfoInputDto input)
        {
            var data = _powerGroupRepository.GetPowerGroupByMenu(input.id, input.menuname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerGroupOutputDto>>(data.items?.AsQueryable().ProjectTo<PowerGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOutputDto>> GetPowerGroupByOperate(OperateInfoInputDto input)
        {
            var data = _powerGroupRepository.GetPowerGroupByOperate(input.id, input.operatename, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerGroupOutputDto>>(data.items?.AsQueryable().ProjectTo<PowerGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        ///  根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOutputDto>> GetPowerGroupByUser(UserInfoInputDto input)
        {
            var data = _powerGroupRepository.GetPowerGroupByUser(input.id, input.username, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerGroupOutputDto>>(data.items?.AsQueryable().ProjectTo<PowerGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOutputDto>> GetPowerGroupByUserGroup(UserGroupInputDto input)
        {
            var data = _powerGroupRepository.GetPowerGroupByUserGroup(input.id, input.usergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerGroupOutputDto>>(data.items?.AsQueryable().ProjectTo<PowerGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOutputDto>> GetPowerGroupByRoleGroup(RoleGroupInputDto input)
        {
            var data = _powerGroupRepository.GetPowerGroupByRoleGroup(input.id, input.rolegroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<PowerGroupOutputDto>>(data.items?.AsQueryable().ProjectTo<PowerGroupOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }
    }
}