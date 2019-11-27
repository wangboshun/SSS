using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.CoinArticel
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinArticelRepository))]
    public class CoinArticelRepository : Repository<Domain.Coin.CoinArticel.CoinArticel>, ICoinArticelRepository
    {
        public CoinArticelRepository(DbcontextBase context) : base(context)
        {
        }

        public IEnumerable<Domain.Coin.CoinArticel.CoinArticel> GetNews(CoinArticelInputDto input)
        {
            var data = Db.Database.SqlQuery<Domain.Coin.CoinArticel.CoinArticel>($"select Title,Author,Logo,Id,CreateTime from CoinArticel where Category={input.Category} ORDER BY CreateTime desc");
            return data?.Skip(input.pagesize * input.pageindex).Take(input.pagesize);
        }
    }
}