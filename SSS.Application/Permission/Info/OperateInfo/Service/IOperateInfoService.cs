using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Seedwork.Model;

using System.Collections.Generic;

namespace SSS.Application.Permission.Info.OperateInfo.Service
{
    public interface IOperateInfoService : IQueryService<Domain.Permission.Info.OperateInfo.OperateInfo, OperateInfoInputDto, OperateInfoOutputDto>
    {
        OperateInfoOutputDto AddOperateInfo(OperateInfoInputDto input);

        /// <summary>
        ///     获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        List<OperateInfoTreeOutputDto> GetChildrenById(string operateid);

        bool DeleteOperateInfo(string id);
        Pages<List<OperateInfoOutputDto>> GetListOperateInfo(OperateInfoInputDto input);

        /// <summary>
        ///     根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        Pages<List<OperateInfoOutputDto>> GetOperateByPowerGroup(PowerGroupInputDto input);
    }
}