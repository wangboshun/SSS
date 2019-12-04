using Microsoft.Extensions.DependencyInjection;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;
using SSS.Domain.Permission.Info.UserInfo.Dto;

namespace SSS.Infrastructure.Repository.Permission.Info.UserInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoRepository))]
    public class UserInfoRepository : Repository<Domain.Permission.Info.UserInfo.UserInfo>, IUserInfoRepository
    {
        public readonly List<UserInfoTreeOutputDto> Tree;

        public UserInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<UserInfoTreeOutputDto>();
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
        /// 根据Parent获取子节点  方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.Info.UserInfo.UserInfo> source, UserInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.Info.UserInfo.UserInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                UserInfoTreeOutputDto model = new UserInfoTreeOutputDto
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

        /// <summary>
        ///  根据Parent获取子节点  方法2-1
        /// </summary>
        /// <param name="originalList"></param>
        /// <returns></returns>
        public static List<UserInfoTreeOutputDto> CreateNewTree(List<Domain.Permission.Info.UserInfo.UserInfo> originalList)
        {
            List<UserInfoTreeOutputDto> nodes = originalList.Where(v => v.ParentId == "0" && v.IsDelete == 0).
                Select(x => new UserInfoTreeOutputDto()
                {
                    id = x.Id,
                    username = x.UserName,
                    parentid = x.ParentId,
                    createtime = x.CreateTime,
                    Item = new List<UserInfoTreeOutputDto>()
                }).ToList();

            foreach (UserInfoTreeOutputDto node in nodes)
            {
                node.Item = GetAllLeaves(node, originalList);
            }
            return nodes;
        }

        /// <summary>
        ///  根据Parent获取子节点  方法2-2
        /// </summary>
        /// <param name="val"></param>
        /// <param name="originalList"></param>
        /// <returns></returns>
        public static List<UserInfoTreeOutputDto> GetAllLeaves(UserInfoTreeOutputDto val, List<Domain.Permission.Info.UserInfo.UserInfo> originalList)
        {
            List<UserInfoTreeOutputDto> nodes = originalList.Where(v => v.ParentId == val.id && v.IsDelete == 0).
                Select(x => new UserInfoTreeOutputDto()
                {
                    id = x.Id,
                    username = x.UserName,
                    parentid = x.ParentId,
                    createtime = x.CreateTime,
                    Item = new List<UserInfoTreeOutputDto>()
                }).ToList();

            foreach (UserInfoTreeOutputDto node in nodes)
            {
                node.Item = GetAllLeaves(node, originalList);
            }
            return nodes;
        }
    }
}