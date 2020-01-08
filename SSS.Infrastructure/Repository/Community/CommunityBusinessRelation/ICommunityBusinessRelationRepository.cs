using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;

using System.Collections.Generic;

namespace SSS.Infrastructure.Repository.Community.CommunityBusinessRelation
{
    public interface ICommunityBusinessRelationRepository : IRepository<SSS.Domain.Community.CommunityBusinessRelation.CommunityBusinessRelation>
    {
        /// <summary>
        /// 根据社区Id获取相关业务
        /// </summary>
        /// <param name="communityid"></param>
        /// <param name="pageindex"></param>
        /// <param name="pagesize"></param>
        /// <returns></returns>
        Pages<IEnumerable<SSS.Domain.Community.CommunityBusiness.CommunityBusiness>>
            GetListCommunityBusinessRelation(string communityid, string communityname, int pageindex = 0, int pagesize = 0);
    }
}