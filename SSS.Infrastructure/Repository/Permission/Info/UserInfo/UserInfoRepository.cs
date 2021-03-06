using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.Info.UserInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoRepository))]
    public class UserInfoRepository : Repository<Domain.Permission.Info.UserInfo.UserInfo>, IUserInfoRepository
    {
        public readonly List<UserInfoTreeOutputDto> Tree;
        private static readonly string field = "u";

        public UserInfoRepository(PermissionDbContext context) : base(context)
        {
            Tree = new List<UserInfoTreeOutputDto>();
        }

        /// <summary>
        /// 根据Parent获取子节点 方法2-1
        /// </summary>
        /// <param name="originalList"></param>
        /// <returns></returns>
        public static List<UserInfoTreeOutputDto> CreateNewTree(
            List<Domain.Permission.Info.UserInfo.UserInfo> originalList)
        {
            var nodes = originalList.Where(v => v.ParentId == "0" && v.IsDelete == 0).Select(x =>
                new UserInfoTreeOutputDto
                {
                    id = x.Id,
                    username = x.UserName,
                    parentid = x.ParentId,
                    createtime = x.CreateTime,
                    Item = new List<UserInfoTreeOutputDto>()
                }).ToList();

            foreach (var node in nodes) node.Item = GetAllLeaves(node, originalList);
            return nodes;
        }

        /// <summary>
        /// 根据Parent获取子节点 方法2-2
        /// </summary>
        /// <param name="val"></param>
        /// <param name="originalList"></param>
        /// <returns></returns>
        public static List<UserInfoTreeOutputDto> GetAllLeaves(UserInfoTreeOutputDto val,
            List<Domain.Permission.Info.UserInfo.UserInfo> originalList)
        {
            var nodes = originalList.Where(v => v.ParentId == val.id && v.IsDelete == 0).Select(x =>
                new UserInfoTreeOutputDto
                {
                    id = x.Id,
                    username = x.UserName,
                    parentid = x.ParentId,
                    createtime = x.CreateTime,
                    Item = new List<UserInfoTreeOutputDto>()
                }).ToList();

            foreach (var node in nodes) node.Item = GetAllLeaves(node, originalList);
            return nodes;
        }

        /// <summary>
        /// 获取用户下的所有下级
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public List<UserInfoTreeOutputDto> GetChildrenById(string userid)
        {
            GetParent(DbSet.ToList(), null, userid);

            return Tree;
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联用户
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.UserInfo.UserInfo>> GetUserByPowerGroup(string powergroupid,
            string powergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT {0}   FROM
	                	UserInfo AS u
	                    INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                    INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ugr.UserGroupId
	                    INNER JOIN RoleGroupPowerGroupRelation AS rgpgr ON rgpgr.RoleGroupId = ugrgr.RoleGroupId
                        INNER JOIN PowerGroup AS pg ON pg.Id = rgpgr.PowerGroupId
                    WHERE
	                    u.IsDelete = 0
	                    AND ugr.IsDelete = 0
	                    AND ugrgr.IsDelete = 0
	                    AND rgpgr.IsDelete = 0
                        AND pg.IsDelete = 0 ";

            if (!string.IsNullOrWhiteSpace(powergroupid))
                sql += $" AND pg.Id='{powergroupid}'";

            if (!string.IsNullOrWhiteSpace(powergroupname))
                sql += $" AND pg.PowerGroupName='{powergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND pg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色Id或名称，遍历关联用户
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.UserInfo.UserInfo>> GetUserByRole(string roleid,
            string rolename, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT {0}   FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroupRelation AS rgr ON rgr.RoleGroupId = ugrgr.RoleGroupId
	                INNER JOIN RoleInfo AS r ON r.Id = rgr.RoleId
                WHERE
	                u.IsDelete = 0
	                AND ugr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND r.IsDelete = 0
	                AND rgr.IsDelete =0 ";

            if (!string.IsNullOrWhiteSpace(roleid))
                sql += $" AND r.Id='{roleid}'";

            if (!string.IsNullOrWhiteSpace(rolename))
                sql += $" AND r.RoleName='{rolename}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND r.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据角色组Id或名称，遍历关联用户
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.UserInfo.UserInfo>> GetUserByRoleGroup(string rolegroupid,
            string rolegroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT {0}   FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroupRoleGroupRelation AS ugrgr ON ugrgr.UserGroupId = ugr.UserGroupId
	                INNER JOIN RoleGroup AS rg ON rg.Id = ugrgr.RoleGroupId
                WHERE
	                u.IsDelete = 0
	                AND ugr.IsDelete = 0
	                AND ugrgr.IsDelete = 0
	                AND rg.IsDelete = 0";

            if (!string.IsNullOrWhiteSpace(rolegroupid))
                sql += $" AND rg.Id='{rolegroupid}'";

            if (!string.IsNullOrWhiteSpace(rolegroupname))
                sql += $" AND rg.RoleGroupName='{rolegroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND rg.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <returns></returns>
        public Pages<IEnumerable<Domain.Permission.Info.UserInfo.UserInfo>> GetUserByUserGroup(string usergroupid,
            string usergroupname, string parentid = "", int pageindex = 0, int pagesize = 0)
        {
            var sql = @"SELECT {0}   FROM
	                UserInfo AS u
	                INNER JOIN UserGroupRelation AS ugr ON u.id = ugr.UserId
	                INNER JOIN UserGroup AS ur ON ugr.UserGroupId = ur.Id
                WHERE
	                u.IsDelete=0
	                AND ur.IsDelete=0
	                AND ugr.IsDelete=0 ";

            if (!string.IsNullOrWhiteSpace(usergroupid))
                sql += $" AND ur.Id='{usergroupid}'";

            if (!string.IsNullOrWhiteSpace(usergroupname))
                sql += $" AND ur.UserGroupName='{usergroupname}'";

            if (!string.IsNullOrWhiteSpace(parentid))
                sql += $" AND ur.ParentId='{parentid}'";

            return GetPage(sql, field, pageindex, pagesize);
        }

        /// <summary>
        /// 根据Parent获取子节点 方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.UserInfo.UserInfo> source, UserInfoTreeOutputDto node,
            string id)
        {
            var list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                var model = new UserInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    username = item.UserName,
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