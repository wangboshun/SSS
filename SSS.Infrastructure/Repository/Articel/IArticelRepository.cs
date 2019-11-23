using SSS.Domain.Articel.Dto;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Articel
{
    public interface IArticelRepository : IRepository<Domain.Articel.Articel>
    {
        IEnumerable<Domain.Articel.Articel> GetNews(ArticelInputDto input);
    }
}