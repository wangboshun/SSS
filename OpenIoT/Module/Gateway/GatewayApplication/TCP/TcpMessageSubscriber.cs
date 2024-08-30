using Furion.EventBus;
using Furion.JsonSerialization;
using GatewayEntity;

namespace GatewayApplication.TCP
{
    public class TcpMessageSubscriber : IEventSubscriber
    {
        public TcpMessageSubscriber()
        {

        }

        [EventSubscribe("Tcp:Online")]
        public async Task TcpOnline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }
        
        [EventSubscribe("Tcp:Offline")]
        public async Task TcpOffline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Tcp:PropertiesReport")]
        public async Task TcpPropertiesReport(EventHandlerExecutingContext context)
        {
            Console.WriteLine($"Tcp:Report--->{context.Source.Payload.ToString()}");
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        } 
    }
}
