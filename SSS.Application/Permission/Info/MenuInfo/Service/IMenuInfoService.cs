using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.MenuInfo.Service
{
    public interface IMenuInfoService : IQueryService<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoInputDto, MenuInfoOutputDto>
    {
        MenuInfoOutputDto AddMenuInfo(MenuInfoInputDto input);

        bool DeleteMenuInfo(string id);

        /// <summary>
        /// ��ȡ�˵��µ������¼�
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        List<MenuInfoTreeOutputDto> GetChildren(string menuid);

        Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ����������˵�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<MenuInfoOutputDto>> GetMenuByPowerGroup(PowerGroupInputDto input);

        bool UpdateMenuInfo(MenuInfoInputDto input);
    }
}