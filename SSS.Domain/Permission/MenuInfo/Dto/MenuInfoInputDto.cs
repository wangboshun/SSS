using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.MenuInfo.Dto
{
    public class MenuInfoInputDto : InputDtoBase
    {
        public string parentid { set; get; }

        public string menuname { set; get; }
    }
}
