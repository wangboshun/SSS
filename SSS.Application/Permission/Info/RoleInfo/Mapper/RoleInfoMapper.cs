using AutoMapper;

using SSS.Domain.Permission.Info.RoleInfo.Dto;

namespace SSS.Application.Permission.Info.RoleInfo.Mapper
{
    public class RoleInfoProfile : Profile
    {
        public RoleInfoProfile()
        {
            CreateMap<Domain.Permission.Info.RoleInfo.RoleInfo, RoleInfoOutputDto>();
            CreateMap<RoleInfoInputDto, Domain.Permission.Info.RoleInfo.RoleInfo>();
        }
    }
}