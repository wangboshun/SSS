using System;
using System.Collections.Generic;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.RoleInfo.Dto
{
    public class RoleInfoOutputDto : OutputDtoBase
    {
        public string name { set; get; }
    }

    public class RoleInfoTreeOutputDto
    {
        public string id { get; set; }

        public string rolename { get; set; }

        public string parentid { get; set; }

        public DateTime createtime { get; set; }

        public List<RoleInfoTreeOutputDto> Item { get; set; }

        public RoleInfoTreeOutputDto()
        {
            Item = new List<RoleInfoTreeOutputDto>();
        }
    }
}
