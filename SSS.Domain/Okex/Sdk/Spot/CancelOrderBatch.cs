﻿using System;
using System.Collections.Generic;
using System.Text;

namespace SSS.Domain.Okex.Sdk.Spot
{
    public class CancelOrderBatch
    {
        /// <summary>
        /// 币种
        /// </summary>
        public string instrument_id { get; set; }
        /// <summary>
        /// 订单编号，每次只能撤销同一币对下的最多4笔订单
        /// </summary>
        public List<long> order_ids { get; set; }
    }
}
