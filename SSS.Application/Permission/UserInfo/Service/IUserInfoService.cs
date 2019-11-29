using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.UserInfo.Service
{
    public interface IUserInfoService : IQueryService<Domain.Permission.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>
    {
        void AddUserInfo(UserInfoInputDto input);
        UserInfoOutputDto GetByUserName(UserInfoInputDto input);
        Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input);

        /// <summary>
        /// ��ȡ�û��µ������¼�
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        List<UserInfoTreeOutputDto> GetChildrenById(string userid);

        /// <summary>
        /// ��ȡ�û�������Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        object GetUserPermission(string userid);

        void DeleteUserInfo(UserInfoInputDto input);
    }
}