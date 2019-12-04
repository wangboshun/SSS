using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.OperateInfo.Service
{
    public interface IOperateInfoService : IQueryService<Domain.Permission.Info.OperateInfo.OperateInfo,
        OperateInfoInputDto, OperateInfoOutputDto>
    {
        void AddOperateInfo(OperateInfoInputDto input);

        /// <summary>
        ///     获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);

        Pages<List<OperateInfoOutputDto>> GetListOperateInfo(OperateInfoInputDto input);
    }
}