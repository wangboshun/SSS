using System;
using System.Collections.Generic;
using SSS.Domain.Permission.UserInfo.Dto;
using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.Permission.OperateInfo
{
    public class OperateInfo : Entity
    {
        public string OperateName { set; get; }

        public string ParentId { set; get; }
    }

    public class OperateInfoTreeOutputDto
    {
        public string id { get; set; }

        public string operatename { get; set; }

        public string parentid { get; set; }

        public DateTime createtime { get; set; }

        public List<OperateInfoTreeOutputDto> Item { get; set; }

        public OperateInfoTreeOutputDto()
        {
            Item = new List<OperateInfoTreeOutputDto>();
        }
    }
}