using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.Articel
{
    [DIService(ServiceLifetime.Scoped, typeof(IArticelRepository))]
    public class ArticelRepository : Repository<SSS.Domain.Articel.Articel>, IArticelRepository
    {
        public ArticelRepository(DbcontextBase context) : base(context)
        {
        }
    }
}