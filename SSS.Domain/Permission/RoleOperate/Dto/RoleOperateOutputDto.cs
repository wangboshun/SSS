using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.RoleOperate.Dto
{
    public class RoleOperateOutputDto : OutputDtoBase
    {
        public string operateid { set; get; }

        public string operatename { set; get; } 
        public string roleid { set; get; }
        public string rolename { set; get; }

    }
}
