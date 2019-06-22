using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Seedwork.Events;
using SSS.Domain.Seedwork.EventStore;
using SSS.Infrastructure.Repository.EventStore;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Json;

namespace SSS.Application.Seedwork.EventStore
{
    [DIService(ServiceLifetime.Scoped, typeof(IEventStore))]
    public class EventStore : IEventStore
    {
        private readonly IEventStoreRepository _repository;

        public EventStore(IEventStoreRepository repository)
        {
            _repository = repository;
        }

        public void Save<T>(T @event) where T : Event
        {
            var json = @event.ToJson();

            var model = new SSS.Domain.Seedwork.Model.EventStore(
                @event.MsgType,
                json,
                "userid");

            _repository.Add(model);
        }
    }
}
