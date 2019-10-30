using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Articel.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;

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
            return Db.Database.SqlQuery<Domain.Articel.Articel>("select Title,Author,Logo,Id,CreateTime from Articel").Skip(input.pagesize * (input.pageindex > 0 ? input.pageindex - 1 : 0)).Take(input.pagesize);
        }

        public IEnumerable<Domain.Articel.Articel> GetQuickNews(ArticelInputDto input)
        {
            return Db.Database.SqlQuery<Domain.Articel.Articel>("select Title,Author,Logo,Id,CreateTime from Articel").Skip(input.pagesize * (input.pageindex > 0 ? input.pageindex - 1 : 0)).Take(input.pagesize);
        }
    }
}