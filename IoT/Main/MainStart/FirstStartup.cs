using System.IO.Compression;

using Common.Ext; 
using Common.Ext.Jwt;

using Furion;

using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.ResponseCompression;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Primitives;

using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace Start;

[AppStartup(999)]
public class FirstStartup : AppStartup
{
    public void ConfigureServices(IServiceCollection services)
    {
        services.AddResponseCompression(options =>
        {
            options.EnableForHttps = true;
            options.Providers.Add<BrotliCompressionProvider>();
            options.Providers.Add<GzipCompressionProvider>();
        });
        services.Configure<BrotliCompressionProviderOptions>(options =>
        {
            options.Level = CompressionLevel.SmallestSize;
        });

        services.Configure<GzipCompressionProviderOptions>(options =>
        {
            options.Level = CompressionLevel.SmallestSize;
        });

        services.AddJwt<JwtHandler>();
        services.AddControllers().AddNewtonsoftJson(options =>
        {
            options.SerializerSettings.ContractResolver = new DefaultContractResolver
            {
                NamingStrategy = new SnakeCaseNamingStrategy { ProcessDictionaryKeys = true }
            }; //下划线输出json

            options.SerializerSettings.Formatting = Formatting.None; // 字符串缩进
            options.SerializerSettings.DateFormatString = "yyyy-MM-dd HH:mm:ss"; // 时间格式化
            options.SerializerSettings.NullValueHandling = NullValueHandling.Include; // 忽略所有 null 属性
            // options.SerializerSettings.DefaultValueHandling = DefaultValueHandling.Ignore; // 忽略所有默认值属性
            options.SerializerSettings.Converters.AddClayConverters(); // 粘土对象 Clay 类型序列化支持
        });
        services.AddDynamicApiControllers();
        services.AddSpecificationDocuments();
        services.AddEventBus(); //事件总线
        services.AddMemoryCache(); //缓存
        services.AddCorsAccessor(); //跨域
        services.AddHttpClient(); // http请求业  
        services.AddFreeSql(); //FreeSql
        services.AddFreeRedis(); //FreeRedis
        services.AddLogStore(); //日志     

        //配置文件监听
        ChangeToken.OnChange(() => App.Configuration.GetReloadToken(),
            () => { Console.WriteLine("------配置文件更新------"); });
    }
}