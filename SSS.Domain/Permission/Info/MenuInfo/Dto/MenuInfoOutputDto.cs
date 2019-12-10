using SSS.Domain.Seedwork.Model;

using System;
using System.Collections.Generic;

namespace SSS.Domain.Permission.Info.MenuInfo.Dto
{
    public class MenuInfoOutputDto : OutputDtoBase
    {
        public string menuname { get; set; }
    }
    public class MenuInfoTreeOutputDto
    {
        public string id { get; set; }

        public string menuname { get; set; }

        public string parentid { get; set; }

        public DateTime createtime { get; set; }

        public List<MenuInfoTreeOutputDto> Item { get; set; }

        public MenuInfoTreeOutputDto()
        {
            Item = new List<MenuInfoTreeOutputDto>();
        }
    }
}
