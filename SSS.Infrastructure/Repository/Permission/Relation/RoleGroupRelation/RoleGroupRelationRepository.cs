using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Group.RoleGroup;
using SSS.Domain.Permission.Info.RoleInfo;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupRelationRepository))]
    public class RoleGroupRelationRepository : Repository<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>, IRoleGroupRelationRepository
    {
        public RoleGroupRelationRepository(DbcontextBase context) : base(context)
        {

        } 
    }
}