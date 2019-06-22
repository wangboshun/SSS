using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Trade.Event.Events;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Json;
using SSS.Infrastructure.Util.Log;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Trade.Event.Handlers
{
    [DIService(ServiceLifetime.Scoped, typeof(INotificationHandler<TradeAddEvent>))]
    public class TradeAddEventHandler : INotificationHandler<TradeAddEvent>
    {
        private static ILogger _logger = ApplicationLog.CreateLogger<TradeAddEventHandler>();

        public Task Handle(TradeAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"TradeAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
