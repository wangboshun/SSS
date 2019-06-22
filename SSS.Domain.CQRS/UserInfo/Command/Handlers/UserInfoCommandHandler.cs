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
using SSS.Infrastructure.Util.ID;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.UserInfo.Command.Handlers
{
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<UserInfoAddCommand, bool>))]
    /// <summary>
    /// UserInfoCommandHandler
    /// </summary>
    public class UserInfoCommandHandler : CommandHandler,
         IRequestHandler<UserInfoAddCommand, bool>
    {
        private readonly IUserInfoRepository _repository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public UserInfoCommandHandler(IUserInfoRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> @event,
                                      ILogger<UserInfoCommandHandler> logger
                                      ) : base(uow, logger, bus, @event)
        {
            _logger = logger;
            _repository = repository;
            Bus = bus;
        }
        public Task<bool> Handle(UserInfoAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }

            if (_repository.GetUserInfoByPhone(request.phone) != null)
            {
                Bus.RaiseEvent(new ErrorNotice(request.MsgType, "ÊÖ»úºÅÒÑ´æÔÚ£¡"));
                return Task.FromResult(false);
            }

            var model = new SSS.Domain.UserInfo.UserInfo(request.id, request.uid, request.phone, request.password, request.firstid);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;
            model.Earning = 0;
            model.Commission = 0;

            if (!string.IsNullOrWhiteSpace(request.firstid) && _repository.GetUserInfoByUid(request.firstid) == null)
            {
                Bus.RaiseEvent(new ErrorNotice(request.MsgType, "ÑûÇëÂë´íÎó£¡"));
                return Task.FromResult(false);
            }
            else if (!string.IsNullOrWhiteSpace(request.firstid))
                model.FirstId = request.firstid;

            model.Uid = RandomId.Instance().GetId();

            _repository.Add(model);
            if (!Commit())
                return Task.FromResult(false);

            _logger.LogInformation("UserInfoAddCommand Success");
            Bus.RaiseEvent(new UserInfoAddEvent(model));
            return Task.FromResult(true);
        }
    }
}
