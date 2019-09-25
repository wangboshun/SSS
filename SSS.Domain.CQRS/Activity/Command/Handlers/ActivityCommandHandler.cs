using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Activity.Command.Commands;
using SSS.Domain.CQRS.Activity.Event.Events;
using SSS.Infrastructure.Util.Attribute; 
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.Activity;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Activity.Command.Handlers
{
    /// <summary>
    /// ActivityCommandHandler
    /// </summary>
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<ActivityAddCommand, bool>))]
    public class ActivityCommandHandler : CommandHandler,
         IRequestHandler<ActivityAddCommand, bool>
    {

        private readonly IActivityRepository _repository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public ActivityCommandHandler(IActivityRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<ActivityCommandHandler> logger)
									  : base(uow, logger,bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            Bus = bus;
        }
        public Task<bool> Handle(ActivityAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var model = new SSS.Domain.Activity.Activity(request.id);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _repository.Add(model);
            if (Commit())
            {
                _logger.LogInformation("ActivityAddCommand Success");
                Bus.RaiseEvent(new ActivityAddEvent(model));
            }
            return Task.FromResult(true);
        }
    }
}
