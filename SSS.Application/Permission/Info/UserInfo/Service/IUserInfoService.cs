using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.UserInfo.Service
{
    public interface IUserInfoService : IQueryService<Domain.Permission.Info.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>
    {
        UserInfoOutputDto AddUserInfo(UserInfoInputDto input);

        bool DeleteUserInfo(string id);

        /// <summary>
        /// 删除用户所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        bool DeleteUserPermission(string userid);

        UserInfoOutputDto Login(UserInfoInputDto input);

        /// <summary>
        /// 获取用户下的所有下级
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        List<UserInfoTreeOutputDto> GetChildrenById(string userid);

        Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserInfoOutputDto>> GetUserByPowerGroup(PowerGroupInputDto input);

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserInfoOutputDto>> GetUserByRoleGroup(RoleGroupInputDto input);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserInfoOutputDto>> GetUserByUserGroup(UserGroupInputDto input);

        /// <summary>
        /// 获取用户下所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        object GetUserPermission(string userid);
    }
}