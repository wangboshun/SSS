namespace SSS.Application.System.Generator
{
    public interface IGeneratorCodeService
    {
        /// <summary>
        /// 生成代码
        /// </summary>
        /// <returns></returns>
        bool CreateCode(string class_name_str, string namespace_name_str, string fields_str);
    }
}