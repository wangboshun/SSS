using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.OperateInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;
using SSS.Domain.Permission.OperateInfo;

namespace SSS.Application.Permission.OperateInfo.Service
{
    public interface IOperateInfoService : IQueryService<SSS.Domain.Permission.OperateInfo.OperateInfo, OperateInfoInputDto, OperateInfoOutputDto>
    {
        void AddOperateInfo(OperateInfoInputDto input);

        /// <summary>
        /// 获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);

        Pages<List<OperateInfoOutputDto>> GetListOperateInfo(OperateInfoInputDto input); 
    }
}