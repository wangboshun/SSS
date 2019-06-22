using SSS.Domain.Articel.Dto;
using SSS.Domain.CQRS.Articel.Command.Commands;

namespace SSS.Application.Articel.Profile
{
    public class ArticelProfile : AutoMapper.Profile
    {
        public ArticelProfile()
        {
            CreateMap<SSS.Domain.Articel.Articel, ArticelOutputDto>();

            CreateMap<ArticelInputDto, ArticelAddCommand>()
                .ConstructUsing(input => new ArticelAddCommand(input));
        }
    }
}