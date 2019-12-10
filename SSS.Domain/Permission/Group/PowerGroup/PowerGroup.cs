using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.PowerGroup
{
    public class PowerGroup : Entity
    {
        public string ParentId { set; get; }

        public string PowerGroupName { set; get; }
    }
}