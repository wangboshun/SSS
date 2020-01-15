using SSS.Domain.Seedwork.Model;

using System;
using System.Collections.Generic;

namespace SSS.Domain.Permission.Info.MenuInfo.Dto
{
    public class MenuInfoOutputDto : OutputDtoBase
    {
        public string menuurl { set; get; }
        public string menuname { get; set; }
    }

    public class MenuInfoTreeOutputDto
    {
        public MenuInfoTreeOutputDto()
        {
            Item = new List<MenuInfoTreeOutputDto>();
        }

        public string id { get; set; }

        public string menuname { get; set; }
        public string menuurl { set; get; }

        public string parentid { get; set; }

        public DateTime createtime { get; set; }

        public List<MenuInfoTreeOutputDto> Item { get; set; }
    }
}