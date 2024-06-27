namespace Common.Utils;

public class IPUtils
{
    public static bool IsWhiteIP(string ip, string[] whiteList)
    {
        //如果存在开放所有ip的配置
        if (whiteList.Any(x => x.Contains("0.0.0.0"))) return true;

        //如果在ip白名单内
        if (whiteList.Any(x => x.Contains(ip)))
            return true;

        //如果存在网段设置
        if (whiteList.Any(x => x.Contains('*')))
        {
            var ip2 = ip.Substring(0, ip.LastIndexOf(".")) + ".*";
            if (!whiteList.Any(x => x.Contains(ip2)))
            {
                return false;
            }
        }
        else
        {
            return false;
        }

        return true;
    }
}