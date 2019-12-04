using AutoMapper;

using SSS.Domain.Permission.Info.PowerInfo.Dto;

namespace SSS.Application.Permission.Info.PowerInfo.Mapper
{
    public class PowerInfoProfile : Profile
    {
        public PowerInfoProfile()
        {
            CreateMap<Domain.Permission.Info.PowerInfo.PowerInfo, PowerInfoOutputDto>();

            CreateMap<PowerInfoInputDto, Domain.Permission.Info.PowerInfo.PowerInfo>();
        }
    }
}