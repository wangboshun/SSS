using SSS.Domain.Permission.RoleInfo.Dto;

namespace SSS.Application.Permission.RoleInfo.Mapper
{
    public class RoleInfoProfile : AutoMapper.Profile
    {
        public RoleInfoProfile()
        {
            CreateMap<SSS.Domain.Permission.RoleInfo.RoleInfo, RoleInfoOutputDto>();

            CreateMap<RoleInfoInputDto, SSS.Domain.Permission.RoleInfo.RoleInfo>();
        }
    }
}
