using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Group.PowerGroup.Dto
{
    public class PowerGroupInputDto : InputDtoBase
    {
        public string parentid { set; get; }
        public string powergroupname { set; get; }
    }
}