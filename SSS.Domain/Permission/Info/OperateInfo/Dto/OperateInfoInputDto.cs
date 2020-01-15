using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.OperateInfo.Dto
{
    public class OperateInfoInputDto : InputDtoBase
    {
        public string operatename { set; get; }
        public string powerid { set; get; }
        public string powergroupid { set; get; }
        public string parentid { set; get; }
    }
}