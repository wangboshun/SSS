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
        /// ����Ȩ��Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByPower(PowerInfoInputDto input);

        /// <summary>
        /// ���ݲ˵�Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByMenu(MenuInfoInputDto input);

        /// <summary>
        /// ���ݲ���Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByOperate(OperateInfoInputDto input);

        /// <summary>
        /// �����û�Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByUser(UserInfoInputDto input);

        /// <summary>
        /// �����û���Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByUserGroup(UserGroupInputDto input);

        /// <summary>
        /// ���ݽ�ɫ��Id�����ƣ���������Ȩ����
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerGroupOutputDto>> GetPowerGroupByRoleGroup(RoleGroupInputDto input);
    }
}