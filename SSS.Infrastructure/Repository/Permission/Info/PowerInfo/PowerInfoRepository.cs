using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.PowerInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerInfoRepository))]
    public class PowerInfoRepository : Repository<Domain.Permission.Info.PowerInfo.PowerInfo>, IPowerInfoRepository
    {
        public readonly List<PowerInfoTreeOutputDto> Tree;

        public PowerInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<PowerInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<PowerInfoTreeOutputDto> GetChildren(string menuid)
        {
            GetParent(DbSet.ToList(), null, menuid);

            return Tree;
        }

        /// <summary>
        /// 根据Parent获取子节点  方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.PowerInfo.PowerInfo> source, PowerInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.PowerInfo.PowerInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                PowerInfoTreeOutputDto model = new PowerInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    powername = item.PowerName,
                    parentid = item.ParentId
                };

                GetParent(source, model, item.Id);

                if (node == null)
                    Tree.Add(model);
                else
                    node.Item.Add(model);
            }
        }
    }
}