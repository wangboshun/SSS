using Furion.EventBus;
using Furion.JsonSerialization;
using GatewayEntity;

namespace GatewayApplication.MQTT
{
    public class MqttMessageSubscriber : IEventSubscriber
    {
        public MqttMessageSubscriber()
        {

        } 

        [EventSubscribe("Mqtt:Online")]
        public async Task MqttOnline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }
        
        [EventSubscribe("Mqtt:Offline")]
        public async Task MqttOffline(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }

        [EventSubscribe("Mqtt:PropertiesReport")]
        public async Task MqttPropertiesReport(EventHandlerExecutingContext context)
        {
            var report = JSON.Deserialize<ReportEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        } 
    }
}
