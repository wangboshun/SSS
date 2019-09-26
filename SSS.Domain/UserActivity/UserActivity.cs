using SSS.Domain.Seedwork.Model;
using System;

namespace SSS.Domain.UserActivity
{
    public class UserActivity : Entity
    {
        public UserActivity(string id,string ActivityId, string WechatName)
        {
            this.Id = id;
            this.ActivityId = ActivityId;
            this.WechatName = WechatName;
        } 

        public string ActivityId { set; get; }

        public string WechatName { set; get; }

        public int GroupNumber { set; get; }

        public int Status { set; get; }
    }
}