using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.PowerInfo.Service
{
    public interface IPowerInfoService : IQueryService<Domain.Permission.Info.PowerInfo.PowerInfo, PowerInfoInputDto, PowerInfoOutputDto>
    {
        PowerInfoOutputDto AddPowerInfo(PowerInfoInputDto input);

        Pages<List<PowerInfoOutputDto>> GetListPowerInfo(PowerInfoInputDto input);

        bool DeletePowerInfo(string id);

        /// <summary>
        /// ����Ȩ����Id�����ƣ���������Ȩ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerInfoOutputDto>> GetPowerByPowerGroup(PowerGroupInputDto input);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ���������Ȩ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerInfoOutputDto>> GetPowerByRoleGroup(RoleGroupInputDto input);

        /// <summary>
        /// �����û���Id�����ƣ���������Ȩ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerInfoOutputDto>> GetPowerByUserGroup(UserGroupInputDto input);

        /// <summary>
        /// �����û�Id�����ƣ���������Ȩ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerInfoOutputDto>> GetPowerByUser(UserInfoInputDto input);

        /// <summary>
        ///     ��ȡȨ���µ������¼�
        /// </summary>
        /// <param name="powerid"></param>
        /// <returns></returns>
        List<PowerInfoTreeOutputDto> GetChildren(string powerid);
    }
}