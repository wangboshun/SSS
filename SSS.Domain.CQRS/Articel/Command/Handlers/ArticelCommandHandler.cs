using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Articel.Command.Commands;
using SSS.Domain.CQRS.Articel.Event.Events;
using SSS.Infrastructure.Util.Attribute;
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.Articel;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Articel.Command.Handlers
{
    /// <summary>
    /// ArticelCommandHandler
    /// </summary>
    [DIService(ServiceLifetime.Scoped,
       typeof(IRequestHandler<ArticelAddCommand, bool>))]
    public class ArticelCommandHandler : CommandHandler,
         IRequestHandler<ArticelAddCommand, bool>
    {

        private readonly IArticelRepository _repository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public ArticelCommandHandler(IArticelRepository repository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<ArticelCommandHandler> logger)
                                      : base(uow, logger, bus, Notice)
        {
            _logger = logger;
            _repository = repository;
            Bus = bus;
        }
        public Task<bool> Handle(ArticelAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var model = new SSS.Domain.Articel.Articel(request.id);
            model.CreateTime = DateTime.Now;
            model.IsDelete = 0;

            _repository.Add(model);
            if (Commit())
            {
                _logger.LogInformation("ArticelAddCommand Success");
                Bus.RaiseEvent(new ArticelAddEvent(model));
            }
            return Task.FromResult(true);
        }
    }
}
