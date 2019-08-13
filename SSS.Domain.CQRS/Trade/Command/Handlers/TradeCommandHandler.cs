using MediatR;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Domain.CQRS.Trade.Command.Commands;
using SSS.Domain.CQRS.Trade.Event.Events;
using SSS.Domain.Seedwork.Command;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.Seedwork.UnitOfWork;
using SSS.Infrastructure.Repository.Trade;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Domain.CQRS.Trade.Command.Handlers
{
    [DIService(ServiceLifetime.Scoped,
        typeof(IRequestHandler<TradeNullCommand, bool>),
        typeof(IRequestHandler<TradeAddCommand, bool>),
        typeof(IRequestHandler<TradeUpdateCommand, bool>))]
    /// <summary>
    /// TradeCommandHandler
    /// </summary>
    public class TradeCommandHandler : CommandHandler,
         IRequestHandler<TradeAddCommand, bool>,
         IRequestHandler<TradeUpdateCommand, bool>,
         IRequestHandler<TradeNullCommand, bool>
    {

        private readonly ITradeRepository _traderepository;
        private readonly IEventBus Bus;
        private readonly ILogger _logger;

        public TradeCommandHandler(ITradeRepository traderepository,
                                      IUnitOfWork uow,
                                      IEventBus bus,
                                      INotificationHandler<ErrorNotice> Notice,
                                      ILogger<TradeCommandHandler> logger
                                      ) : base(uow, logger, bus, Notice)
        {
            _logger = logger;
            _traderepository = traderepository;
            Bus = bus;
        }

        public Task<bool> Handle(TradeAddCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }
            var trade = new SSS.Domain.Trade.Trade(
                request.id, request.coin,
                request.size, request.first_price, 0,
                request.side, request.first_trade_status, 0,
                request.first_trade_no, "", request.first_time, null, request.ktime, request.userid);
            trade.CreateTime = DateTime.Now;
            trade.IsDelete = 0;
            _traderepository.Add(trade);

            if (Commit())
            {
                _logger.LogInformation("TradeAddCommand Success");
                Bus.RaiseEvent(new TradeAddEvent(trade));
                return Task.FromResult(true);
            }
            return Task.FromResult(false);
        }

        public Task<bool> Handle(TradeUpdateCommand request, CancellationToken cancellationToken)
        {
            if (!request.IsValid())
            {
                NotifyValidationErrors(request);
                return Task.FromResult(false);
            }

            var trade = _traderepository.GetByTradeNo(request.first_trade_no);
            if (trade == null)
            {
                _logger.LogInformation("TradeUpdateCommand Error Trade Is Null");
                return Task.FromResult(false);
            }

            trade.Last_Price = request.last_price;
            trade.Last_Trade_No = request.last_trade_no;
            trade.Last_Trade_Status = request.last_trade_status;
            trade.Last_Time = request.last_time;

            if (trade.Side.Equals("buy"))
                trade.Income = (((trade.Last_Price / trade.First_Price) - 1) * 100).ToString() + "%";
            else
                trade.Income = (((trade.First_Price / trade.Last_Price) - 1) * 100).ToString() + "%";

            _traderepository.Update(trade);

            if (Commit())
            {
                _logger.LogInformation("TradeUpdateCommand Success");
                Bus.RaiseEvent(new TradeUpdateEvent(trade));
                return Task.FromResult(true);
            }
            return Task.FromResult(false);
        }

        public Task<bool> Handle(TradeNullCommand request, CancellationToken cancellationToken)
        {
            var trade = _traderepository.GetByTradeNo(request.first_trade_no);
            if (trade == null)
            {
                _logger.LogInformation("TradeNullCommand Error Because Is Null");
                return Task.FromResult(false);
            }
            trade.Last_Price = request.last_price;
            trade.Last_Time = DateTime.Now;
            _logger.LogInformation("TradeNullCommand Success");
            Bus.RaiseEvent(new TradeNullEvent(trade));
            return Task.FromResult(true);
        }
    }
}
