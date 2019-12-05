using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupPowerGroupRelation;
using SSS.Infrastructure.Repository.Permission.Relation.UserPowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupService))]
    public class PowerGroupService :
        QueryService<Domain.Permission.Group.PowerGroup.PowerGroup, PowerGroupInputDto, PowerGroupOutputDto>,
        IPowerGroupService
    {
        private readonly IUserPowerGroupRelationRepository _userPowerGroupRelation;
        private readonly IPowerGroupRelationRepository _powerGroupRelationRepository;
        private readonly IPowerGroupMenuRelationRepository _powerGroupMenuRelationRepository;
        private readonly IPowerGroupOperateRelationRepository _powerGroupOperateRelationRepository;
        private readonly IUserGroupPowerGroupRelationRepository _userGroupPowerGroupRelationRepository;

        public PowerGroupService(IMapper mapper,
            IPowerGroupRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupInputDto> validator,
            IUserPowerGroupRelationRepository userPowerGroupRelation,
            IPowerGroupRelationRepository powerGroupRelationRepository,
            IPowerGroupMenuRelationRepository powerGroupMenuRelationRepository,
            IPowerGroupOperateRelationRepository powerGroupOperateRelationRepository,
            IUserGroupPowerGroupRelationRepository userGroupPowerGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _userPowerGroupRelation = userPowerGroupRelation;
            _powerGroupRelationRepository = powerGroupRelationRepository;
            _powerGroupMenuRelationRepository = powerGroupMenuRelationRepository;
            _powerGroupOperateRelationRepository = powerGroupOperateRelationRepository;
            _userGroupPowerGroupRelationRepository = userGroupPowerGroupRelationRepository;
        }

        public void AddPowerGroup(PowerGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.PowerGroup.PowerGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerGroupOutputDto>> GetListPowerGroup(PowerGroupInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerGroup(PowerGroupInputDto input)
        {
            Repository.Remove(input.id);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerGroupRelationInputDto input)
        {
            return _powerGroupRelationRepository.GetPowerGroupByPower(input);
        }

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(PowerGroupMenuRelationInputDto input)
        {
            return _powerGroupMenuRelationRepository.GetPowerGroupByMenu(input);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(PowerGroupOperateRelationInputDto input)
        {
            return _powerGroupOperateRelationRepository.GetPowerGroupByOperate(input);
        }

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserPowerGroupRelationOutputDto>> GetPowerGroupByUser(UserPowerGroupRelationInputDto input)
        {
            return _userPowerGroupRelation.GetPowerGroupByUser(input);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(UserGroupPowerGroupRelationInputDto input)
        {
            return _userGroupPowerGroupRelationRepository.GetPowerGroupByUserGroup(input);
        }
    }
}