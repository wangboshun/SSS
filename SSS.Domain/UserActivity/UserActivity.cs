using SSS.Domain.Seedwork.Model;
using System;

namespace SSS.Domain.UserActivity
{
    public class UserActivity : Entity
    {
        public UserActivity(string id)
        {
            this.Id = id; 
        } 

        public string Activityid { set; get; }

        public string Wechatname { set; get; }

        public int Groupnumber { set; get; }

        public int Status { set; get; }
    }
}