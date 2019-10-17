using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;

namespace SSS.Infrastructure.Repository.DigitalCurrency
{
    [DIService(ServiceLifetime.Scoped, typeof(IDigitalCurrencyRepository))]
    public class DigitalCurrencyRepository : Repository<SSS.Domain.DigitalCurrency.DigitalCurrency>, IDigitalCurrencyRepository
    {
        public DigitalCurrencyRepository(DbcontextBase context) : base(context)
        {
        }
    }
}