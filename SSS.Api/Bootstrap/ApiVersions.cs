using System.ComponentModel;

namespace SSS.Api.Bootstrap
{
    /// <summary>
    ///     版本号
    /// </summary>
    public enum ApiVersions
    {
        /// <summary>
        /// v1 版本
        /// </summary>
        [Description("系统接口")] v1 = 1,


        /// <summary>
        /// v2 版本
        /// </summary>
        [Description("权限接口")] v2 = 2,


        /// <summary>
        /// v3 版本
        /// </summary>
        [Description("量化接口")] v3 = 3,

        /// <summary>
        /// v4 版本
        /// </summary>
        [Description("社区接口")] v4 = 4
    }
}
