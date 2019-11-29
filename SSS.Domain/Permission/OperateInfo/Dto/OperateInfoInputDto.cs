using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.OperateInfo.Dto
{
    public class OperateInfoInputDto : InputDtoBase
    {
        public string operatename { set; get; }

        public string parentid { set; get; }
    }
}
