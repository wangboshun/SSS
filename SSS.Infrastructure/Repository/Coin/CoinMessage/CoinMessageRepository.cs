using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinMessage.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Linq;

namespace SSS.Infrastructure.Repository.Coin.CoinMessage
{
    [DIService(ServiceLifetime.Singleton, typeof(ICoinMessageRepository))]
    public class CoinMessageRepository : Repository<Domain.Coin.CoinMessage.CoinMessage>, ICoinMessageRepository
    {
        public CoinMessageRepository(CoinDbContext context) : base(context)
        {
        }

        public IQueryable<Domain.Coin.CoinMessage.CoinMessage> GetPageOrderByAsc(CoinMessageInputDto input, ref int count)
        {
            count = DbSet.Count(x => x.CreateTime > DateTime.Now);
            var data = DbSet.Where(x => x.CreateTime > DateTime.Now && x.IsDelete == 0).OrderBy(x => x.CreateTime)
                .Skip(input.pagesize * (input.pageindex > 1 ? input.pageindex - 1 : 0)).Take(input.pagesize);

            return data;
        }
    }
}