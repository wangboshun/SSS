using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    public interface IPowerGroupService : IQueryService<Domain.Permission.Group.PowerGroup.PowerGroup,
        PowerGroupInputDto, PowerGroupOutputDto>
    {
        void AddPowerGroup(PowerGroupInputDto input);

        Pages<List<PowerGroupOutputDto>> GetListPowerGroup(PowerGroupInputDto input);

        void DeletePowerGroup(PowerGroupInputDto input);

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerGroupRelationInputDto input);

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupMenuRelationOutputDto>> GetPowerGroupByMenu(PowerGroupMenuRelationInputDto input);

        /// <summary>
        /// 根据操作Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOperateRelationOutputDto>> GetPowerGroupByOperate(PowerGroupOperateRelationInputDto input);

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserPowerGroupRelationOutputDto>> GetPowerGroupByUser(UserPowerGroupRelationInputDto input);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupPowerGroupRelationOutputDto>> GetPowerGroupByUserGroup(UserGroupPowerGroupRelationInputDto input);

    }
}