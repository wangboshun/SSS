using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.MenuInfo.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.MenuInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoRepository))]
    public class MenuInfoRepository : Repository<SSS.Domain.Permission.MenuInfo.MenuInfo>, IMenuInfoRepository
    {
        public readonly List<MenuInfoTreeOutputDto> Tree;

        public MenuInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<MenuInfoTreeOutputDto>();
        }

        /// <summary>
        /// ��ȡ�ڵ���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public List<MenuInfoTreeOutputDto> GetChildren(MenuInfoInputDto input)
        {
            GetParent(DbSet.ToList(), null, input.id);

            return Tree;
        }

        /// <summary>
        /// ����Parent��ȡ�ӽڵ�  ����1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.MenuInfo.MenuInfo> source, MenuInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.MenuInfo.MenuInfo> list = source.Where(x => x.ParentId == id).ToList();
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