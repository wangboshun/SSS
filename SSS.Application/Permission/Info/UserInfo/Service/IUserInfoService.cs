using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
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
        ///     ��ȡ�û��µ������¼�
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        List<UserInfoTreeOutputDto> GetChildrenById(string userid);

        /// <summary>
        ///     ��ȡ�û�������Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        object GetUserPermission(string userid);

        /// <summary>
        ///     ɾ���û�����Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        bool DeleteUserPermission(string userid);

        void DeleteUserInfo(UserInfoInputDto input);

        /// <summary>
        /// �����û���Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserInfoInputDto>> GetUserByUserGroup(UserGroupInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<UserInfoOutputDto>> GetUserByPowerGroup(PowerGroupInputDto input);
    }
}