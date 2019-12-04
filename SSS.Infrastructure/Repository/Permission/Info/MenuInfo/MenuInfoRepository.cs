using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.MenuInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoRepository))]
    public class MenuInfoRepository : Repository<Domain.Permission.Info.MenuInfo.MenuInfo>, IMenuInfoRepository
    {
        public readonly List<MenuInfoTreeOutputDto> Tree;

        public MenuInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<MenuInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<MenuInfoTreeOutputDto> GetChildren(string menuid)
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
        private void GetParent(List<Domain.Permission.Info.MenuInfo.MenuInfo> source, MenuInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.MenuInfo.MenuInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                MenuInfoTreeOutputDto model = new MenuInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    menuname = item.MenuName,
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