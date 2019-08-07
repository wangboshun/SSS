using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserApi.Command.Commands;
using SSS.Domain.CQRS.UserApi.Event.Events;
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.UserApi;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserApi.Command.Handlers
{
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<UserApiAddCommand, bool>),
       typeof(IRequestHandler<UserApiUpdateCommand, bool>))]
    /// <summary>
    /// UserApiCommandHandler
    /// </summary>
    public class UserApiCommandHandler : CommandHandler,
         IRequestHandler<UserApiAddCommand, bool>,
         IRequestHandler<UserApiUpdateCommand, bool>
    {

        private readonly IUserApiRepository _repository; 
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public UserApiCommandHandler(IUserApiRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<UserApiCommandHandler> logger)
                                      : base(uow, logger, bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            Bus = bus;
        }
        public Task<bool> Handle(UserApiAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var model = new SSS.Domain.UserApi.UserApi(request.id, request.ApiKey, request.Secret, request.PassPhrase, request.UserId,request.Status);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _repository.Add(model);
            if (Commit())
            {
                _logger.LogInformation("UserApiAddCommand Success");
                Bus.RaiseEvent(new UserApiAddEvent(model));
            }
            return Task.FromResult(false);
        }

        public Task<bool> Handle(UserApiUpdateCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
             
            var model = new SSS.Domain.UserApi.UserApi(request.id, request.ApiKey, request.Secret, request.PassPhrase, request.UserId, request.Status);

            _repository.Update(model);
            if (Commit())
            {
                _logger.LogInformation("UserApiUpdateCommand Success");
                Bus.RaiseEvent(new UserApiUpdateEvent(model));
            }
            return Task.FromResult(false);
        }
    }
}
