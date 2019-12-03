using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.RolePowerRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IRolePowerRelationRepository))]
    public class RolePowerRelationRepository : Repository<Domain.Permission.Relation.RolePowerRelation.RolePowerRelation>, IRolePowerRelationRepository
    {
        public RolePowerRelationRepository(DbcontextBase context) : base(context)
        {
        }
    }
}