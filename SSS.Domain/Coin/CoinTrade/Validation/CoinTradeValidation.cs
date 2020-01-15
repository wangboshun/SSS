using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinTrade.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Coin.CoinTrade.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CoinTradeInputDto>))]
    public class CoinTradeAddValidation : AbstractValidator<CoinTradeInputDto>
    {
        public CoinTradeAddValidation()
        {
            RuleSet("Insert", () => { });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}