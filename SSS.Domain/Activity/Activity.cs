using SSS.Domain.Seedwork.Model;

using System;

namespace SSS.Domain.Activity
{
    public class Activity : Entity
    {
        public string Title { set; get; }

        public int Grouptotal { set; get; }

        public string Qrcode { set; get; }

        public string Content { set; get; }

        public int Maxjoin { set; get; }

        public DateTime Starttime { set; get; }

        public DateTime Endtime { set; get; }
    }
}