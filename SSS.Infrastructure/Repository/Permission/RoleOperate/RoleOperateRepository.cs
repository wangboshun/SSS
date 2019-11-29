using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.RoleOperate
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleOperateRepository))]
    public class RoleOperateRepository : Repository<SSS.Domain.Permission.RoleOperate.RoleOperate>, IRoleOperateRepository
    {
        public RoleOperateRepository(DbcontextBase context) : base(context)
        {
        }

        /// <summary>
        /// 删除角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        public bool DeleteRoleOperateByRole(string roleid)
        {
            return DeleteList(x => x.RoleId.Equals(roleid), true);
        }

        /// <summary>
        /// 获取角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleOperateOutputDto> GetRoleOperateByRole(string roleid)
        {
            var result = Db.RoleOperate.Where(x => x.RoleId.Equals(roleid) && x.IsDelete == 0).Join(Db.OperateInfo,
                role => role.OperateId, operate => operate.Id,
                (role, operate) => new RoleOperateOutputDto
                {
                    id = role.Id,
                    operateid = operate.Id,
                    operatename = operate.OperateName,
                    createtime = role.CreateTime
                }).ToList();
            return result;
        }
    }
}