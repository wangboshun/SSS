using MediatR;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserActivity.Event.Events;
using SSS.Infrastructure.Util.Json;
using SSS.Infrastructure.Util.Log;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserActivity.Event.Handlers
{
    [SSS.Infrastructure.Util.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<UserActivityAddEvent>))]
    public class UserActivityAddEventHandler : INotificationHandler<UserActivityAddEvent>
    {
        private static ILogger _logger = ApplicationLog.CreateLogger<UserActivityAddEventHandler>();

        public Task Handle(UserActivityAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"UserActivityAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}