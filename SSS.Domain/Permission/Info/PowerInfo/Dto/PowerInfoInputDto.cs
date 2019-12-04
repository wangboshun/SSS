using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.PowerInfo.Dto
{
    public class PowerInfoInputDto : InputDtoBase
    {
        public string powername { set; get; }

        public int powertype { set; get; }

        public string powergroupid { set; get; }

        public string parentid { set; get; }
    }
}
