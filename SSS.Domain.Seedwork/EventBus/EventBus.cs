using MediatR;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Seedwork.Events;
using SSS.Domain.Seedwork.EventStore;
using SSS.Infrastructure.Util.Attribute;
using System.Threading.Tasks;

namespace SSS.Domain.Seedwork.EventBus
{
    [DIService(ServiceLifetime.Scoped, typeof(IEventBus))]
    public class EventBus : IEventBus
    {
        private readonly IMediator _mediator;
        private readonly IEventStore _eventstore;

        public EventBus(IMediator mediator, IEventStore eventstore)
        {
            _mediator = mediator;
            _eventstore = eventstore;
        }

        public Task SendCommand<T>(T command) where T : Command.Command
        {
            return _mediator.Send(command);
        }

        public Task RaiseEvent<T>(T @event) where T : Event
        {
            _eventstore.Save(@event);
            return _mediator.Publish(@event);
        }
    }
}
