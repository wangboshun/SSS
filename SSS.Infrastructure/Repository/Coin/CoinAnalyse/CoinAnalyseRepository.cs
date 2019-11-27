using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinAnalyse.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Linq;

namespace SSS.Infrastructure.Repository.Coin.CoinAnalyse
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinAnalyseRepository))]
    public class CoinAnalyseRepository : Repository<SSS.Domain.Coin.CoinAnalyse.CoinAnalyse>, ICoinAnalyseRepository
    {
        public CoinAnalyseRepository(DbcontextBase context) : base(context)
        {
        }

        public IQueryable<Domain.Coin.CoinAnalyse.CoinAnalyse> GetPageOrderByAsc(CoinAnalyseInputDto input, ref int count)
        {
            count = DbSet.Count(x => x.IsDelete == 0);
            var data = DbSet.OrderByDescending(x => x.CloseRange).Where(x => x.IsDelete == 0)
                .Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex : 0)).Take(input.pagesize);

            return data;
        }
    }
}