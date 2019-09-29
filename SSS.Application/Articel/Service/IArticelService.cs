using SSS.Domain.Articel.Dto;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;

namespace SSS.Application.Articel.Service
{
    public interface IArticelService
    {
        void AddArticel(ArticelInputDto input);

        Pages<List<ArticelOutputDto>> GetListArticel(ArticelInputDto input);
    }
}