using System;
using System.ComponentModel;
using System.Reflection;

namespace SSS.Infrastructure.Util.Enum
{
    public static class EnumHelp
    {
        public static string GetDescription(this System.Enum val)
        {
            Type type = val.GetType();   //获取类型  
            MemberInfo[] memberInfos = type.GetMember(val.ToString());   //获取成员  
            if (memberInfos.Length > 0)
            {
                DescriptionAttribute[] attrs = memberInfos[0].GetCustomAttributes(typeof(DescriptionAttribute), false) as DescriptionAttribute[];   //获取描述特性  
                if (attrs != null && attrs.Length > 0)
                {
                    return attrs[0].Description;    //返回当前描述
                }
            }
            return val.ToString();
        }
    }
}
