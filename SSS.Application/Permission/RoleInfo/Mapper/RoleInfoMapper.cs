using SSS.Domain.Permission.RoleInfo.Dto;

namespace SSS.Application.Permission.RoleInfo.Mapper
{
    public class RoleInfoProfile : AutoMapper.Profile
    {
        public RoleInfoProfile()
        {
            CreateMap<SSS.Domain.Permission.RoleInfo.RoleInfo, RoleInfoOutputDto>()
                .ForMember(x => x.name, y => y.MapFrom(z => z.RoleName));

            CreateMap<RoleInfoInputDto, SSS.Domain.Permission.RoleInfo.RoleInfo>();
        }
    }
}
