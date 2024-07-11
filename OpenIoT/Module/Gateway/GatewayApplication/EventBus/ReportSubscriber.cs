using DeviceEntity;

using Furion.EventBus;
using Furion.JsonSerialization;

using GatewayEntity;

namespace GatewayApplication.EventBus
{
    public class ReportSubscriber : IEventSubscriber
    {
        public ReportSubscriber()
        {

        }

        [EventSubscribe("Http:Online")]
        public async Task HttpOnline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Http:Report")]
        public async Task HttpProperties(EventHandlerExecutingContext context)
        {
            Console.WriteLine($"Http:Report--->{context.Source.Payload.ToString()}");
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Mqtt:Online")]
        public async Task MqttOnline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Mqtt:Report")]
        public async Task MqttProperties(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Device:Shadow")]
        public async Task LastInfo(EventHandlerExecutingContext context)
        {
            Console.WriteLine($"Device:Shadow--->{context.Source.Payload.ToString()}");
            var shadow = JSON.Deserialize<DeviceShadowEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }
    }
}
