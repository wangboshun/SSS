using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.RoleMenu
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleMenuRepository))]
    public class RoleMenuRepository : Repository<SSS.Domain.Permission.RoleMenu.RoleMenu>, IRoleMenuRepository
    {
        public RoleMenuRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 获取角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleMenuOutputDto> GetRoleMenuByRole(string roleid)
        {
            var result = Db.RoleMenu.Where(x => x.RoleId.Equals(roleid) && x.IsDelete == 0).Join(Db.MenuInfo,
                role => role.MenuId, menu => menu.Id,
                (role, menu) => new RoleMenuOutputDto
                {
                    id = role.Id,
                    menuid = menu.Id,
                    menuname = menu.MenuName,
                    createtime = role.CreateTime
                }).ToList();
            return result;
        }

        /// <summary>
        /// 删除角色下的所有菜单
        /// </summary>
        /// <param name="roleid"></param>
        public bool DeleteRoleMenuByRole(string roleid)
        {
            return DeleteList(x => x.RoleId.Equals(roleid), true);
        }
    }
}