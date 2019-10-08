using SSS.Domain.Articel.Dto;
using SSS.Domain.CQRS.Articel.Command.Commands;

namespace SSS.Application.Articel.Mapper
{
    public class ArticelProfile : AutoMapper.Profile
    {
        public ArticelProfile()
        {
            CreateMap<SSS.Domain.Articel.Articel, ArticelOutputDto>();

            CreateMap<ArticelInputDto, SSS.Domain.Articel.Articel>();

            CreateMap<ArticelInputDto, ArticelAddCommand>()
                .ConstructUsing(input => new ArticelAddCommand(input));
        }
    }
}
