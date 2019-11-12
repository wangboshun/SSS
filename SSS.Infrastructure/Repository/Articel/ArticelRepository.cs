using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Articel.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Articel
{
    [DIService(ServiceLifetime.Scoped, typeof(IArticelRepository))]
    public class ArticelRepository : Repository<Domain.Articel.Articel>, IArticelRepository
    {
        public ArticelRepository(DbcontextBase context) : base(context)
        {
        }

        public IEnumerable<Domain.Articel.Articel> GetNews(ArticelInputDto input)
        {
            var data = Db.Database.SqlQuery<Domain.Articel.Articel>($"select Title,Author,Logo,Id,CreateTime from Articel where Category={input.Category} ORDER BY CreateTime desc");
            return data?.Skip(input.pagesize * (input.pageindex > 0 ? input.pageindex - 1 : 0)).Take(input.pagesize);
        }
    }
}