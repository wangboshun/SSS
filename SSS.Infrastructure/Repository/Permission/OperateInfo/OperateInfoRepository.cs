using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.OperateInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IOperateInfoRepository))]
    public class OperateInfoRepository : Repository<SSS.Domain.Permission.OperateInfo.OperateInfo>, IOperateInfoRepository
    {
        public OperateInfoRepository(DbcontextBase context) : base(context)
        {
        }
    }
}