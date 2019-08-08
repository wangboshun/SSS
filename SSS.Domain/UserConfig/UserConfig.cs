using SSS.Domain.Seedwork.Model;
using System;

namespace SSS.Domain.UserConfig
{
    public class UserConfig : Entity
    {
        public UserConfig(string id, string UserId, string coin, int ktime, double size, int profit, int loss, int status, int IsDelete)
        {
            Id = id;
            Coin = coin;
            Size = size;
            Ktime = ktime;
            Size = size;
            Profit = profit;
            this.UserId = UserId;
            Loss = loss;
            Status = status;
            this.IsDelete = IsDelete;
        }

        public string Coin { set; get; }

        public int Ktime { set; get; }

        public double Size { set; get; }

        public string UserId { set; get; }

        public int Profit { set; get; }

        public int Loss { set; get; }

        public int Status { set; get; } 
    }
}