using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.RoleMenu.Dto
{
    public class RoleMenuOutputDto : OutputDtoBase
    {
        public string menuname { set; get; }
        public string menuid { set; get; }
        public string roleid { set; get; }
        public string rolename { set; get; }
    }
}
