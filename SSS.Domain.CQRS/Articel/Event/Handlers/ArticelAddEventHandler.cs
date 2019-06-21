using MediatR;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Articel.Event.Events;
using SSS.Infrastructure.Util.Json;
using SSS.Infrastructure.Util.Log;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Articel.Event.Handlers
{
    [SSS.Domain.Seedwork.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<ArticelAddEvent>))]
    public class ArticelAddEventHandler : INotificationHandler<ArticelAddEvent>
    {
        private static ILogger _logger = ApplicationLog.CreateLogger<ArticelAddEventHandler>();

        public Task Handle(ArticelAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"ArticelAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
