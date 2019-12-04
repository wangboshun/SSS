using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.PowerInfo.Service
{
    public interface IPowerInfoService : IQueryService<Domain.Permission.Info.PowerInfo.PowerInfo, PowerInfoInputDto,
        PowerInfoOutputDto>
    {
        void AddPowerInfo(PowerInfoInputDto input);

        Pages<List<PowerInfoOutputDto>> GetListPowerInfo(PowerInfoInputDto input);

        void DeletePowerInfo(PowerInfoInputDto input);

        /// <summary>
        /// ����Ȩ����Id�����ƣ���������Ȩ��
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<PowerPowerGroupRelationOutputDto>> GetPowerListByGroup(PowerPowerGroupRelationInputDto input);

        /// <summary>
        ///     ��ȡȨ���µ������¼�
        /// </summary>
        /// <param name="powerid"></param>
        /// <returns></returns>
        List<PowerInfoTreeOutputDto> GetChildren(string powerid);
    }
}