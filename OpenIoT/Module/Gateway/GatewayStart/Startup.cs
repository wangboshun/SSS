using Furion;
using GatewayApplication.EventBus;
using GatewayApplication.HTTP;
using GatewayApplication.MQTT;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.DependencyInjection;

namespace GatewayStart
{
    [AppStartup(100)]
    public class Startup : AppStartup
    {
        public void ConfigureServices(IServiceCollection services)
        {
            // 注册 EventBus 服务
            services.AddEventBus(builder =>
            {
                // 注册  事件订阅者
                builder.AddSubscriber<MessageSubscriber>(); 
                builder.AddSubscriber<HttpMessageSubscriber>(); 
                builder.AddSubscriber<MqttMessageSubscriber>(); 
            });
        }
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
        }
    }
}