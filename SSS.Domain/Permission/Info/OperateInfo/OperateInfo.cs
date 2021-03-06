using SSS.Domain.Seedwork.Model;

using System;
using System.Collections.Generic;

namespace SSS.Domain.Permission.Info.OperateInfo
{
    public class OperateInfo : Entity
    {
        public string OperateName { set; get; }

        public string ParentId { set; get; }
    }

    public class OperateInfoTreeOutputDto
    {
        public OperateInfoTreeOutputDto()
        {
            Item = new List<OperateInfoTreeOutputDto>();
        }

        public DateTime createtime { get; set; }
        public string id { get; set; }

        public List<OperateInfoTreeOutputDto> Item { get; set; }
        public string operatename { get; set; }

        public string parentid { get; set; }
    }
}