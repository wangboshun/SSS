using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.CoinMessage.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Linq;

namespace SSS.Infrastructure.Repository.CoinMessage
{
    [DIService(ServiceLifetime.Scoped, typeof(ICoinMessageRepository))]
    public class CoinMessageRepository : Repository<Domain.CoinMessage.CoinMessage>, ICoinMessageRepository
    {
        public CoinMessageRepository(DbcontextBase context) : base(context)
        {
        }

        public IQueryable<Domain.CoinMessage.CoinMessage> GetPageOrderByAsc(CoinMessageInputDto input, ref int count)
        {
            count = DbSet.Count(x => x.CreateTime > DateTime.Now);
            var data = DbSet.Where(x => x.CreateTime > DateTime.Now).OrderBy(x => x.CreateTime)
                .Skip(input.pagesize * input.pageindex).Take(input.pagesize);

            return data;
        }
    }
}