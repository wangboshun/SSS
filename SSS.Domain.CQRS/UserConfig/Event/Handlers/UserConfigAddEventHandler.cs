using MediatR;
using Microsoft.Extensions.Logging; 
using SSS.Infrastructure.Util.Json; 
using SSS.Domain.CQRS.UserConfig.Event.Events;
using System.Threading;
using System.Threading.Tasks;
using SSS.Infrastructure.Util.Log;

namespace SSS.Domain.CQRS.UserConfig.Event.Handlers
{
    [SSS.Infrastructure.Util.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<UserConfigAddEvent>))]
    public class UserConfigAddEventHandler : INotificationHandler<UserConfigAddEvent>
    { 
	   private static ILogger _logger = ApplicationLog.CreateLogger<UserConfigAddEventHandler>();

        public Task Handle(UserConfigAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"UserConfigAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
