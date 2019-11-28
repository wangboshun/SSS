using SSS.Domain.Permission.OperateInfo.Dto;

namespace SSS.Application.Permission.OperateInfo.Mapper
{
    public class OperateInfoProfile : AutoMapper.Profile
    {
        public OperateInfoProfile()
        {
            CreateMap<SSS.Domain.Permission.OperateInfo.OperateInfo, OperateInfoOutputDto>()
                .ForMember(x => x.name, y => y.MapFrom(z => z.OperateName));

            CreateMap<OperateInfoInputDto, SSS.Domain.Permission.OperateInfo.OperateInfo>();
        }
    }
}
