using MediatR;
using Microsoft.Extensions.Logging; 
using SSS.Infrastructure.Util.Json; 
using SSS.Domain.CQRS.Activity.Event.Events;
using System.Threading;
using System.Threading.Tasks;
using SSS.Infrastructure.Util.Log;

namespace SSS.Domain.CQRS.Activity.Event.Handlers
{
    [SSS.Infrastructure.Util.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<ActivityAddEvent>))]
    public class ActivityAddEventHandler : INotificationHandler<ActivityAddEvent>
    { 
	   private static ILogger _logger = ApplicationLog.CreateLogger<ActivityAddEventHandler>();

        public Task Handle(ActivityAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"ActivityAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
