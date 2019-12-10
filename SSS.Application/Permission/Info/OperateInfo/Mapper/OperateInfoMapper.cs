using AutoMapper;

using SSS.Domain.Permission.Info.OperateInfo.Dto;

namespace SSS.Application.Permission.Info.OperateInfo.Mapper
{
    public class OperateInfoProfile : Profile
    {
        public OperateInfoProfile()
        {
            CreateMap<Domain.Permission.Info.OperateInfo.OperateInfo, OperateInfoOutputDto>();
            CreateMap<OperateInfoInputDto, Domain.Permission.Info.OperateInfo.OperateInfo>();
        }
    }
}