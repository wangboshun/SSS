using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.DigitalCurrency.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Linq;

namespace SSS.Infrastructure.Repository.DigitalCurrency
{
    [DIService(ServiceLifetime.Scoped, typeof(IDigitalCurrencyRepository))]
    public class DigitalCurrencyRepository : Repository<SSS.Domain.DigitalCurrency.DigitalCurrency>, IDigitalCurrencyRepository
    {
        public DigitalCurrencyRepository(DbcontextBase context) : base(context)
        {

        }

        public IQueryable<Domain.DigitalCurrency.DigitalCurrency> GetPageOrderByAsc(DigitalCurrencyInputDto input, ref int count)
        {
            count = DbSet.Count(x => x.IsDelete == 0);
            var data = DbSet.OrderByDescending(x => x.CloseRange).Where(x => x.IsDelete == 0)
                .Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex : 0)).Take(input.pagesize);

            return data;
        }
    }
}