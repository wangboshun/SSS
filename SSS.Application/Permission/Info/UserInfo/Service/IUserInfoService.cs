using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.UserInfo.Service
{
    public interface IUserInfoService : IQueryService<Domain.Permission.Info.UserInfo.UserInfo, UserInfoInputDto,
        UserInfoOutputDto>
    {
        void AddUserInfo(UserInfoInputDto input);
        UserInfoOutputDto GetByUserName(UserInfoInputDto input);
        Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input);

        /// <summary>
        ///     获取用户下的所有下级
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        List<UserInfoTreeOutputDto> GetChildrenById(string userid);

        /// <summary>
        ///     获取用户下所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        object GetUserPermission(string userid);

        /// <summary>
        ///     删除用户所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        bool DeleteUserPermission(string userid);

        void DeleteUserInfo(UserInfoInputDto input);

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserGroupRelationOutputDto>> GetUserListByGroup(UserGroupRelationInputDto input);

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserPowerGroupRelationOutputDto>> GetUserListByPowerGroup(UserPowerGroupRelationInputDto input);
    }
}