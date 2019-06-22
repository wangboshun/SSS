using MediatR;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserInfo.Event.Events;
using SSS.Infrastructure.Util.Http;
using SSS.Infrastructure.Util.Json;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserInfo.Event.Handlers
{
    [SSS.Infrastructure.Util.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<UserInfoAddEvent>))]
    public class UserInfoAddEventHandler : INotificationHandler<UserInfoAddEvent>
    {
        private static ILogger _logger;

        public UserInfoAddEventHandler()
        {
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<UserInfoAddEventHandler>));
        }

        public Task Handle(UserInfoAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"UserInfoAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
