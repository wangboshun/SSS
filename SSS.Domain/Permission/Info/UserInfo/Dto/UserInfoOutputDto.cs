using SSS.Domain.Seedwork.Model;

using System;
using System.Collections.Generic;

namespace SSS.Domain.Permission.Info.UserInfo.Dto
{
    public class UserInfoOutputDto : OutputDtoBase
    {
        public string username { set; get; }
    }

    public class UserInfoTreeOutputDto
    {
        public UserInfoTreeOutputDto()
        {
            Item = new List<UserInfoTreeOutputDto>();
        }

        public DateTime createtime { get; set; }
        public string id { get; set; }

        public List<UserInfoTreeOutputDto> Item { get; set; }
        public string parentid { get; set; }
        public string username { get; set; }
    }
}