using SSS.Domain.Seedwork.Model;
using System;

namespace SSS.Domain.UserConfig
{
    public class UserConfig : Entity
    {
        public UserConfig(string id)
        {
            this.Id = id; 
        } 

        public string Coin { set; get; }

        public string Side { set; get; }

        public int Ktime { set; get; }

        public double Size { set; get; }

        public string UserId { set; get; }

        public int Profit { set; get; }

        public int Loss { set; get; }
    }
}