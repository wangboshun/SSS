using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Ef;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Community.CommunityBusinessRelation
{
    [DIService(ServiceLifetime.Scoped, typeof(ICommunityBusinessRelationRepository))]
    public class CommunityBusinessRelationRepository : Repository<SSS.Domain.Community.CommunityBusinessRelation.CommunityBusinessRelation>, ICommunityBusinessRelationRepository
    {
        public CommunityBusinessRelationRepository(CommunityDbContext context) : base(context)
        {
        }

        /// <summary>
        /// 根据社区Id获取相关业务
        /// </summary> 
        /// <returns></returns>
        public Pages<IEnumerable<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>> GetListCommunityBusinessRelation(string communityid, string communityname, int pageindex = 0, int pagesize = 0)
        {
            string sql = @"SELECT {0} FROM
	                CommunityBusinessRelation AS r
	                INNER JOIN CommunityInfo AS i ON r.CommunityId = i.Id
	                INNER JOIN CommunityBusiness AS b ON r.BusinessId = b.Id ";

            string field = @"b";

            if (!string.IsNullOrWhiteSpace(communityid))
                sql += $" AND i.Id='{communityid}'";

            if (!string.IsNullOrWhiteSpace(communityname))
                sql += $" AND i.Name='{communityname}'";

            int count = Db.Database.Count(string.Format(sql, $" count( DISTINCT {field}.Id ) "));

            if (pageindex > 0 && pagesize > 0)
            {
                string limit = " limit {1},{2} ";
                var data = Db.Database.SqlQuery<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>(string.Format(sql + limit, $" DISTINCT {field}.* ", pageindex == 1 ? 0 : pageindex * pagesize + 1, pagesize));
                return new Pages<IEnumerable<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>>(data, count);
            }

            {
                var data = Db.Database.SqlQuery<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>(string.Format(sql, $" DISTINCT {field}.* "));
                return new Pages<IEnumerable<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>>(data, count);
            }
        }
    }
}