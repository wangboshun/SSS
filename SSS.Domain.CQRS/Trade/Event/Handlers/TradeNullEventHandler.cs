﻿using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Trade.Event.Events;
using SSS.Domain.Seedwork.Attribute;
using SSS.Infrastructure.Util;
using SSS.Infrastructure.Util.Json;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Trade.Event.Handlers
{
    [DIService(ServiceLifetime.Scoped, typeof(INotificationHandler<TradeNullEvent>))]
    public class TradeNullEventHandler : INotificationHandler<TradeNullEvent>
    {
        private static ILogger _logger;

        public TradeNullEventHandler()
        {
            _logger = (ILogger)HttpContextService.Current.RequestServices.GetService(typeof(ILogger<TradeNullEventHandler>));
        }

        public Task Handle(TradeNullEvent @event, CancellationToken cancellationToken)
        {
            _logger.LogInformation($"TradeNullEventHandler {@event.ToJson()}");
            return Task.CompletedTask;
        }
    }
}
