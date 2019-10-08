using SSS.Application.Seedwork.Service;
using SSS.Domain.Articel.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.Articel.Service
{
    public interface IArticelService : IQueryService<SSS.Domain.Articel.Articel, ArticelInputDto, ArticelOutputDto>
    {
        void AddArticel(ArticelInputDto input);

        Pages<List<ArticelOutputDto>> GetListArticel(ArticelInputDto input);
    }
}