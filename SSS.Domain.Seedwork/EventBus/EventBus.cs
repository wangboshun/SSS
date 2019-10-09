using MediatR;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Seedwork.Events; 
using SSS.Infrastructure.Util.Attribute;
using System.Threading.Tasks;

namespace SSS.Domain.Seedwork.EventBus
{
    [DIService(ServiceLifetime.Scoped, typeof(IEventBus))]
    public class EventBus : IEventBus
    {
        private readonly IMediator _mediator; 

        public EventBus(IMediator mediator)
        {
            _mediator = mediator;
        }

        public Task SendCommand<T>(T command) where T : Command.Command
        {
            return _mediator.Send(command);
        }

        public Task RaiseEvent<T>(T @event) where T : Event
        {
            return _mediator.Publish(@event);
        }
    }
}
