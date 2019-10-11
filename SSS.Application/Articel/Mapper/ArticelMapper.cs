using SSS.Domain.Articel.Dto;

namespace SSS.Application.Articel.Mapper
{
    public class ArticelProfile : AutoMapper.Profile
    {
        public ArticelProfile()
        {
            CreateMap<SSS.Domain.Articel.Articel, ArticelOutputDto>();

            CreateMap<ArticelInputDto, SSS.Domain.Articel.Articel>();
        }
    }
}
