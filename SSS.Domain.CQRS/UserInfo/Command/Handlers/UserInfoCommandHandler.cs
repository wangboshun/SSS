using AutoMapper;
using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.UserInfo.Command.Commands;
using SSS.Domain.CQRS.UserInfo.Event.Events;
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.UserInfo;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserInfo.Command.Handlers
{
    /// <summary>
    /// UserInfoCommandHandler
    /// </summary>
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<UserInfoAddCommand, bool>))]
    public class UserInfoCommandHandler : CommandHandler,
         IRequestHandler<UserInfoAddCommand, bool>
    {

        private readonly IUserInfoRepository _repository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;
        private readonly IMapper _mapper;

        public UserInfoCommandHandler(
                                      IMapper mapper,
                                      IUserInfoRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<UserInfoCommandHandler> logger)
                                      : base(uow, logger, bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            _mapper = mapper;
            Bus = bus;
        }
        public Task<bool> Handle(UserInfoAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }

            var model = _mapper.Map<SSS.Domain.UserInfo.UserInfo>(request.inputDto);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _repository.Add(model);
            if (Commit())
            {
                _logger.LogInformation("UserInfoAddCommand Success");
                Bus.RaiseEvent(new UserInfoAddEvent(model));
                return Task.FromResult(true);
            }
            return Task.FromResult(false);
        }
    }
}
