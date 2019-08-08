using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserConfig.Command.Commands;
using SSS.Domain.CQRS.UserConfig.Event.Events;
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.UserConfig;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserConfig.Command.Handlers
{
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<UserConfigAddCommand, bool>),
       typeof(IRequestHandler<UserConfigUpdateCommand, bool>))]
    /// <summary>
    /// UserConfigCommandHandler
    /// </summary>
    public class UserConfigCommandHandler : CommandHandler,
         IRequestHandler<UserConfigAddCommand, bool>,
         IRequestHandler<UserConfigUpdateCommand, bool>
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
            var config = new Domain.UserConfig.UserConfig(request.id, request.userid, request.coin, request.ktime,
                request.size, request.profit, request.loss, request.status, request.isdelete);
            config.CreateTime = DateTime.Now; 

            _repository.Add(config);
            if (Commit())
            {
                _logger.LogInformation("UserConfigAddCommand Success");
                Bus.RaiseEvent(new UserConfigAddEvent(config));
            }
            return Task.FromResult(true);
        }

        public Task<bool> Handle(UserConfigUpdateCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var config = new Domain.UserConfig.UserConfig(request.id, request.userid, request.coin, request.ktime,
                request.size, request.profit, request.loss, request.status, request.isdelete);

            _repository.Update(config);
            if (Commit())
            {
                _logger.LogInformation("UserConfigUpdateCommand Success");
                Bus.RaiseEvent(new UserConfigUpdateEvent(config));
            }
            return Task.FromResult(true);
        }
    }
}
