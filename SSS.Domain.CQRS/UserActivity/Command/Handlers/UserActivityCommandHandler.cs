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
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using SSS.Infrastructure.Repository.Activity;

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

        private static readonly object Lock = new object();
        private readonly IUserActivityRepository _repository;
        private readonly IActivityRepository _activityrepository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public UserActivityCommandHandler(IUserActivityRepository repository,
            IActivityRepository activityrepository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<UserActivityCommandHandler> logger)
                                      : base(uow, logger, bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            _activityrepository = activityrepository;
            Bus = bus;
        }
        public Task<bool> Handle(UserActivityAddCommand request, CancellationToken cancellationToken)
        {
            lock (Lock)
            {
                List<Domain.UserActivity.UserActivity> list = new List<Domain.UserActivity.UserActivity>();

                if (!request.IsValid())
                {
                    NotifyValidationErrors(request);
                    return Task.FromResult(false);
                }

                var useractivitylist = _repository.GetAll(x => x.ActivityId.Equals(request.activityid)).OrderByDescending(x => x.GroupNumber)
                    .ToList();

                var activity = _activityrepository.Get(request.activityid);

                if (useractivitylist.Count() >= activity.Grouptotal)
                {
                    Bus.RaiseEvent(new ErrorNotice(request.MsgType, "群号已领完！"));
                    return Task.FromResult(false);
                }

                if (useractivitylist.Count()+request.grouptotal >activity.Grouptotal)
                {
                    Bus.RaiseEvent(new ErrorNotice(request.MsgType, $"群号还剩下{activity.Grouptotal- useractivitylist.Count()}个,请重新填写数量！"));
                    return Task.FromResult(false);
                }

                if (request.grouptotal > 0)
                {
                    for (int i = 1; i < request.grouptotal + 1; i++)
                    {
                        var model = new SSS.Domain.UserActivity.UserActivity(Guid.NewGuid().ToString(), request.activityid, request.wechatname);
                        model.GroupNumber = useractivitylist.Any() ? useractivitylist[0].GroupNumber + i : i;
                        model.CreateTime = DateTime.Now;
                        model.IsDelete = 0;
                        list.Add(model);
                    }
                }

                if (_repository.AddActivity(list))
                {
                    _logger.LogInformation("UserActivityAddCommand Success");
                    Bus.RaiseEvent(new UserActivityAddEvent(list));
                    return Task.FromResult(true);
                }
                return Task.FromResult(false);
            }
        }
    }
}
