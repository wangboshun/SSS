using AutoMapper;

using SSS.Domain.Articel.Dto;

namespace SSS.Application.Articel.Mapper
{
    public class ArticelProfile : Profile
    {
        public ArticelProfile()
        {
            CreateMap<Domain.Articel.Articel, ArticelOutputDto>();

            CreateMap<ArticelInputDto, Domain.Articel.Articel>();
        }
    }
}