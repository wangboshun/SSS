using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleInfo.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.RoleInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleInfoRepository))]
    public class RoleInfoRepository : Repository<SSS.Domain.Permission.RoleInfo.RoleInfo>, IRoleInfoRepository
    {
        public readonly List<RoleInfoTreeOutputDto> Tree;

        public RoleInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<RoleInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取角色下的所有下级
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleInfoTreeOutputDto> GetChildren(string roleid)
        {
            GetParent(DbSet.ToList(), null, roleid);

            return Tree;
        }

        /// <summary>
        /// 根据Parent获取子节点 
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.RoleInfo.RoleInfo> source, RoleInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.RoleInfo.RoleInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                RoleInfoTreeOutputDto model = new RoleInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    rolename = item.RoleName,
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