using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.MenuInfo.Dto
{
    public class MenuInfoInputDto : InputDtoBase
    {
        public string parentid { set; get; }
        public string menuname { set; get; }
        public string powerid { set; get; }
        public string powergroupid { set; get; }
    }
}
