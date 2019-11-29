using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.OperateInfo;
using SSS.Infrastructure.Seedwork.DbContext;
using SSS.Infrastructure.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

using System.Collections.Generic;
using System.Linq;

namespace SSS.Infrastructure.Repository.Permission.OperateInfo
{
    [DIService(ServiceLifetime.Scoped, typeof(IOperateInfoRepository))]
    public class OperateInfoRepository : Repository<SSS.Domain.Permission.OperateInfo.OperateInfo>, IOperateInfoRepository
    {
        public readonly List<OperateInfoTreeOutputDto> Tree;

        public OperateInfoRepository(DbcontextBase context) : base(context)
        {
            Tree = new List<OperateInfoTreeOutputDto>();
        }

        /// <summary>
        /// 获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        public List<OperateInfoTreeOutputDto> GetChildrenById(string operateid)
        {
            GetParent(DbSet.ToList(), null, operateid);

            return Tree;
        }

        /// <summary>
        /// 根据Parent获取子节点  方法1
        /// </summary>
        /// <param name="source"></param>
        /// <param name="node"></param>
        /// <param name="id"></param>
        /// GetParent(DbSet.ToList(), null, input.id);
        private void GetParent(List<Domain.Permission.OperateInfo.OperateInfo> source, OperateInfoTreeOutputDto node, string id)
        {
            List<Domain.Permission.OperateInfo.OperateInfo> list = source.Where(x => x.ParentId == id && x.IsDelete == 0).ToList();
            foreach (var item in list)
            {
                OperateInfoTreeOutputDto model = new OperateInfoTreeOutputDto
                {
                    id = item.Id,
                    createtime = item.CreateTime,
                    operatename = item.OperateName,
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