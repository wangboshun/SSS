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

        public List<RoleMenuOutputDto> GetMenuByRole(string roleid)
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

    }
}