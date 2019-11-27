using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.UserInfo.Dto;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.UserInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoRepository))]
    public class UserInfoRepository : Repository<Domain.Permission.UserInfo.UserInfo>, IUserInfoRepository
    {
        public readonly List<UserInfoTreeOutputDto> Tree;

        public UserInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<UserInfoTreeOutputDto>();
        }

        /// <summary>
        /// ��ȡ�ڵ���
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public List<UserInfoTreeOutputDto> GetChildren(UserInfoInputDto input)
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
        private void GetParent(List<Domain.Permission.UserInfo.UserInfo> source, UserInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.UserInfo.UserInfo> list = source.Where(x => x.ParentId == id).ToList();
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
        ///  ����Parent��ȡ�ӽڵ�  ����2-1
        /// </summary>
        /// <param name="originalList"></param>
        /// <returns></returns>
        public static List<UserInfoTreeOutputDto> CreateNewTree(List<Domain.Permission.UserInfo.UserInfo> originalList)
        {
            List<UserInfoTreeOutputDto> nodes = originalList.Where(v => v.ParentId == "0").
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
        ///  ����Parent��ȡ�ӽڵ�  ����2-2
        /// </summary>
        /// <param name="val"></param>
        /// <param name="originalList"></param>
        /// <returns></returns>
        public static List<UserInfoTreeOutputDto> GetAllLeaves(UserInfoTreeOutputDto val, List<Domain.Permission.UserInfo.UserInfo> originalList)
        {
            List<UserInfoTreeOutputDto> nodes = originalList.Where(v => v.ParentId == val.id).
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