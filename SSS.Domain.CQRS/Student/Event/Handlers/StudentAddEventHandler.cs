using MediatR;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Student.Event.Events;
using SSS.Infrastructure.Util.Http;
using SSS.Infrastructure.Util.Json;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Student.Event.Handlers
{
    [SSS.Infrastructure.Util.Attribute.DIService(Microsoft.Extensions.DependencyInjection.ServiceLifetime.Scoped, typeof(INotificationHandler<StudentAddEvent>))]
    public class StudentAddEventHandler : INotificationHandler<StudentAddEvent>
    {
        private static ILogger _logger;

        public StudentAddEventHandler()
        {
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<StudentAddEventHandler>));
        }

        public Task Handle(StudentAddEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"StudentAddEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
