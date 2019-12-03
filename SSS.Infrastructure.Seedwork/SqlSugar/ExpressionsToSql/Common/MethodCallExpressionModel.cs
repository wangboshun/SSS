using System;
using System.Collections.Generic;
namespace SqlSugar
{
    public class MethodCallExpressionModel
    {
        public List<MethodCallExpressionArgs> Args { get; set; }
        public string Name { get; set; }
        public dynamic Data { get; set; }
    }

    public class MethodCallExpressionArgs
    {
        public bool IsMember { get; set; }
        public object MemberName { get; set; }
        public object MemberValue { get; set; }
        public Type Type { get; set; }
    }
}
