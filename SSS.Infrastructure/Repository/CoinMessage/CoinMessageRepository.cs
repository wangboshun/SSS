using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.CoinMessage
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinMessageRepository))]
    public class CoinMessageRepository : Repository<SSS.Domain.CoinMessage.CoinMessage>, ICoinMessageRepository
    {
        public CoinMessageRepository(DbcontextBase context) : base(context)
        {
        }
    }
}