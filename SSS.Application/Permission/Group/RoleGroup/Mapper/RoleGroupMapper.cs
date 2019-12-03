using AutoMapper;

using SSS.Domain.Permission.Group.RoleGroup.Dto;

namespace SSS.Application.Permission.Group.RoleGroup.Mapper
{
    public class RoleGroupProfile : Profile
    {
        public RoleGroupProfile()
        {
            CreateMap<Domain.Permission.Group.RoleGroup.RoleGroup, RoleGroupOutputDto>();

            CreateMap<RoleGroupInputDto, Domain.Permission.Group.RoleGroup.RoleGroup>();
        }
    }
}