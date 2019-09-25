using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserActivity.Command.Commands;
using SSS.Domain.CQRS.UserActivity.Event.Events;
using SSS.Infrastructure.Util.Attribute; 
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.UserActivity;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserActivity.Command.Handlers
{
    /// <summary>
    /// UserActivityCommandHandler
    /// </summary>
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<UserActivityAddCommand, bool>))]
    public class UserActivityCommandHandler : CommandHandler,
         IRequestHandler<UserActivityAddCommand, bool>
    {

        private readonly IUserActivityRepository _repository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public UserActivityCommandHandler(IUserActivityRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<UserActivityCommandHandler> logger)
									  : base(uow, logger,bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            Bus = bus;
        }
        public Task<bool> Handle(UserActivityAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var model = new SSS.Domain.UserActivity.UserActivity(request.id);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _repository.Add(model);
            if (Commit())
            {
                _logger.LogInformation("UserActivityAddCommand Success");
                Bus.RaiseEvent(new UserActivityAddEvent(model));
            }
            return Task.FromResult(true);
        }
    }
}
