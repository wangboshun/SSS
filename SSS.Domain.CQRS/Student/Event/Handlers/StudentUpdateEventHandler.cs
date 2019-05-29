﻿using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using SSS.Api.Seedwork;
using SSS.Domain.CQRS.Student.Event.Events;
using SSS.Domain.Seedwork.Attribute;
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
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<ApiBaseController>));
        }

        public Task Handle(StudentUpdateEvent notification, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"StudentUpdateEventHandler {JsonConvert.SerializeObject(notification)}");
            return Task.CompletedTask;
        }
    }
}