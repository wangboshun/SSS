using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Coin.CoinInfo.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<CoinInfoInputDto>))]
    public class CoinInfoAddValidation : AbstractValidator<CoinInfoInputDto>
    {
        public CoinInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {

            });

            RuleSet("Delete", () =>
            {

            });

            RuleSet("Update", () =>
            {

            });

            RuleSet("Select", () =>
            {

            });
        }
    }
}
