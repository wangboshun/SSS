using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Repository.Community.UserCommunityRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserCommunityRelationRepository))]
    public class UserCommunityRelationRepository : Repository<SSS.Domain.Community.UserCommunityRelation.UserCommunityRelation>, IUserCommunityRelationRepository
    {
        public UserCommunityRelationRepository(CommunityDbContext context) : base(context)
        {
        }
    }
}