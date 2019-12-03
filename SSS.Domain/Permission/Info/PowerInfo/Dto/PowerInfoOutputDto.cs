using System;
using System.Collections.Generic;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.PowerInfo.Dto
{
    public class PowerInfoOutputDto : OutputDtoBase
    {
    }

    public class PowerInfoTreeOutputDto
    {
        public string id { get; set; }

        public string powername { get; set; }

        public string parentid { get; set; }

        public DateTime createtime { get; set; }

        public List<PowerInfoTreeOutputDto> Item { get; set; }

        public PowerInfoTreeOutputDto()
        {
            Item = new List<PowerInfoTreeOutputDto>();
        }
    }
}
