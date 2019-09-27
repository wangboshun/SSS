using MediatR;
using Microsoft.Extensions.Logging; 
using SSS.Infrastructure.Util.Json; 
using SSS.Domain.CQRS.UserInfo.Event.Events;
using System.Threading;
using System.Threading.Tasks;
using SSS.Infrastructure.Util.Log;

namespace SSS.Domain.CQRS.UserInfo.Event.Handlers
{
    [SSS.Infrastructure.Util.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<UserInfoAddEvent>))]
    public class UserInfoAddEventHandler : INotificationHandler<UserInfoAddEvent>
    { 
	   private static ILogger _logger = ApplicationLog.CreateLogger<UserInfoAddEventHandler>();

        public Task Handle(UserInfoAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"UserInfoAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
