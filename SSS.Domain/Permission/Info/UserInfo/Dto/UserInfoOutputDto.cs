using System;
using System.Collections.Generic;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.Info.UserInfo.Dto
{
    public class UserInfoOutputDto : OutputDtoBase
    {

    }

    public class UserInfoTreeOutputDto
    {
        public string id { get; set; }

        public string username { get; set; }

        public string parentid { get; set; }

        public DateTime createtime { get; set; }

        public List<UserInfoTreeOutputDto> Item { get; set; }

        public UserInfoTreeOutputDto()
        {
            Item = new List<UserInfoTreeOutputDto>();
        }
    }
}