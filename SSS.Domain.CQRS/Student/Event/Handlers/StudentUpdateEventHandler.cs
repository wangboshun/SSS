using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Student.Event.Events;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Http;
using SSS.Infrastructure.Util.Json;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Student.Event.Handlers
{
    [DIService(ServiceLifetime.Scoped, typeof(INotificationHandler<StudentUpdateEvent>))]
    public class StudentUpdateEventHandler : INotificationHandler<StudentUpdateEvent>
    {
        private static ILogger _logger;

        public StudentUpdateEventHandler()
        {
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<StudentUpdateEventHandler>));
        }

        public Task Handle(StudentUpdateEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"StudentUpdateEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
