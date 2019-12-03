using AutoMapper;

using SSS.Domain.Permission.Group.PowerGroup.Dto;

namespace SSS.Application.Permission.Group.PowerGroup.Mapper
{
    public class PowerGroupProfile : Profile
    {
        public PowerGroupProfile()
        {
            CreateMap<Domain.Permission.Group.PowerGroup.PowerGroup, PowerGroupOutputDto>();

            CreateMap<PowerGroupInputDto, Domain.Permission.Group.PowerGroup.PowerGroup>();
        }
    }
}