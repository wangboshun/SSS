using SSS.Domain.CQRS.Trade.Validations;
using SSS.Domain.Trade.Dto;

namespace SSS.Domain.CQRS.Trade.Command.Commands
{
    public class TradeAddCommand : TradeCommand
    {
        public TradeAddCommand(TradeInputDto input)
        {
            this.id = input.id;
            this.coin = input.coin;
            this.ktime = input.ktime;
            this.first_trade_no = input.first_trade_no;
            this.first_trade_status = input.first_trade_status;
            this.side = input.side;
            this.size = input.size;
            this.first_time = input.first_time;
            this.first_price = input.first_price;
            this.userid = input.userid;
        }

        public override bool IsValid()
        {
            ValidationResult = new TradeAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
