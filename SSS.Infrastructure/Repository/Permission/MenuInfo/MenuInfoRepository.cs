using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.MenuInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoRepository))]
    public class MenuInfoRepository : Repository<SSS.Domain.Permission.MenuInfo.MenuInfo>, IMenuInfoRepository
    {
        public MenuInfoRepository(DbcontextBase context) : base(context)
        {
        }
    }
}