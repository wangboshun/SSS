using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    public interface IPowerGroupService : IQueryService<Domain.Permission.Group.PowerGroup.PowerGroup,
        PowerGroupInputDto, PowerGroupOutputDto>
    {
        PowerGroupOutputDto AddPowerGroup(PowerGroupInputDto input);

        Pages<List<PowerGroupOutputDto>> GetListPowerGroup(PowerGroupInputDto input);

        bool DeletePowerGroup(string id);

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByPower(PowerInfoInputDto input);

        /// <summary>
        /// 根据菜单Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByMenu(MenuInfoInputDto input);

        /// <summary>
        /// 根据操作Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByOperate(OperateInfoInputDto input);

        /// <summary>
        /// 根据用户Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByUser(UserInfoInputDto input);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByUserGroup(UserGroupInputDto input);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByRoleGroup(RoleGroupInputDto input);
    }
}