using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.OperateInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.OperateInfo.Service
{
    public interface IOperateInfoService : IQueryService<SSS.Domain.Permission.OperateInfo.OperateInfo, OperateInfoInputDto, OperateInfoOutputDto>
    {
        void AddOperateInfo(OperateInfoInputDto input);

        Pages<List<OperateInfoOutputDto>> GetListOperateInfo(OperateInfoInputDto input);
    }
}