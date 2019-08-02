using MediatR;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserApi.Event.Events;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Json;
using SSS.Infrastructure.Util.Log;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserApi.Event.Handlers
{
    [DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<UserApiAddEvent>))]
    public class UserApiAddEventHandler : INotificationHandler<UserApiAddEvent>
    {
        private static ILogger _logger = ApplicationLog.CreateLogger<UserApiAddEventHandler>();

        public Task Handle(UserApiAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"UserApiAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
