using System.ComponentModel;

namespace Common.Utils;

public class EnumUtils
{
    public static string GetDescription(Enum e)
    {
        var name = e.ToString();
        var desc = e.GetType().GetField(name)?.GetCustomAttributes(typeof(DescriptionAttribute), false)
            ?.FirstOrDefault() as DescriptionAttribute;
        return desc?.Description ?? name;
    }
}