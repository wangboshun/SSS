﻿using System;
using System.Collections.Generic;
using System.Text;

namespace SSS.Domain.Okex.Sdk.Ett
{
    public class DefinePrice
    {
        /// <summary>
        /// 该基金产品清算时间
        /// </summary>
        public DateTime date { get; set; }
        /// <summary>
        /// 该基金产品清算时价格
        /// </summary>
        public decimal price { get; set; }
    }
}
