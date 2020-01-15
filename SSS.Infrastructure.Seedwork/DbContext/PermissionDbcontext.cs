using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

using SSS.Domain.Permission.Group.PowerGroup;
using SSS.Domain.Permission.Group.RoleGroup;
using SSS.Domain.Permission.Group.UserGroup;
using SSS.Domain.Permission.Info.MenuInfo;
using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Permission.Info.PowerInfo;
using SSS.Domain.Permission.Info.RoleInfo;
using SSS.Domain.Permission.Info.UserInfo;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation;
using SSS.Domain.Permission.Relation.PowerGroupRelation;
using SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation;
using SSS.Domain.Permission.Relation.RoleGroupRelation;
using SSS.Domain.Permission.Relation.UserGroupRelation;
using SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Infrastructure.Seedwork.DbContext
{
    [DIService(ServiceLifetime.Scoped, typeof(PermissionDbContext))]
    public class PermissionDbContext : DbContextBase
    {
        public PermissionDbContext(IHostEnvironment env, ILoggerFactory factory) : base(env, factory)
        {
        }

        #region Permission

        public DbSet<MenuInfo> MenuInfo { get; set; }
        public DbSet<OperateInfo> OperateInfo { get; set; }
        public DbSet<PowerGroup> PowerGroup { get; set; }
        public DbSet<PowerGroupMenuRelation> PowerGroupMenuRelation { get; set; }
        public DbSet<PowerGroupOperateRelation> PowerGroupOperateRelation { get; set; }
        public DbSet<PowerGroupRelation> PowerGroupRelation { set; get; }
        public DbSet<PowerInfo> PowerInfo { get; set; }
        public DbSet<RoleGroup> RoleGroup { get; set; }
        public DbSet<RoleGroupPowerGroupRelation> RoleGroupPowerGroupRelation { set; get; }
        public DbSet<RoleGroupRelation> RoleGroupRelation { set; get; }
        public DbSet<RoleInfo> RoleInfo { get; set; }
        public DbSet<UserGroup> UserGroup { get; set; }
        public DbSet<UserGroupRelation> UserGroupRelation { get; set; }
        public DbSet<UserGroupRoleGroupRelation> UserGroupRoleGroupRelation { set; get; }
        public DbSet<UserInfo> UserInfo { get; set; }

        #endregion Permission
    }
}