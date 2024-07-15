using DeviceEntity;

using Furion.EventBus;
using Furion.JsonSerialization;

namespace GatewayApplication.EventBus
{
    public class MessageSubscriber : IEventSubscriber
    {
        public MessageSubscriber()
        {

        } 

        [EventSubscribe("Device:Shadow")]
        public async Task Shadow(EventHandlerExecutingContext context)
        {
            Console.WriteLine($"Device:Shadow--->{context.Source.Payload.ToString()}");
            var shadow = JSON.Deserialize<DeviceShadowEntity>(context.Source.Payload.ToString());
            await Task.CompletedTask;
        }
    }
}
