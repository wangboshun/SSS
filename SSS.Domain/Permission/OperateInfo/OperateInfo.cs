using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.OperateInfo
{
    public class OperateInfo : Entity
    {
        public string OperateName { set; get; }

        public string ParentId { set; get; }
    }
}