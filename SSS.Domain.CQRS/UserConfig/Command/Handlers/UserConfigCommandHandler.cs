using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserConfig.Command.Commands;
using SSS.Domain.CQRS.UserConfig.Event.Events;
using SSS.Infrastructure.Util.Attribute;
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.UserConfig;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserConfig.Command.Handlers
{
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<UserConfigAddCommand, bool>))]
    /// <summary>
    /// UserConfigCommandHandler
    /// </summary>
    public class UserConfigCommandHandler : CommandHandler,
         IRequestHandler<UserConfigAddCommand, bool>
    {

        private readonly IUserConfigRepository _repository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public UserConfigCommandHandler(IUserConfigRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<UserConfigCommandHandler> logger)
                                      : base(uow, logger, bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            Bus = bus;
        }
        public Task<bool> Handle(UserConfigAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var model = new Domain.UserConfig.UserConfig(request.id, request.userid, request.coin, request.ktime,
                request.size,request.profit, request.loss);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _repository.Add(model);
            if (Commit())
            {
                _logger.LogInformation("UserConfigAddCommand Success");
                Bus.RaiseEvent(new UserConfigAddEvent(model));
            }
            return Task.FromResult(true);
        }
    }
}
