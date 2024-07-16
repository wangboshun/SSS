using Furion.EventBus;
using Furion.JsonSerialization;
using GatewayEntity;

namespace GatewayApplication.HTTP
{
    public class HttpMessageSubscriber : IEventSubscriber
    {
        public HttpMessageSubscriber()
        {

        }

        [EventSubscribe("Http:Online")]
        public async Task HttpOnline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }
        
        [EventSubscribe("Http:Offline")]
        public async Task HttpOffline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Http:PropertiesReport")]
        public async Task HttpPropertiesReport(EventHandlerExecutingContext context)
        {
            Console.WriteLine($"Http:Report--->{context.Source.Payload.ToString()}");
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        } 
    }
}
