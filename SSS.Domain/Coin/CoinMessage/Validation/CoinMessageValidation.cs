using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinMessage.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Coin.CoinMessage.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CoinMessageInputDto>))]
    public class CoinMessageAddValidation : AbstractValidator<CoinMessageInputDto>
    {
        public CoinMessageAddValidation()
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
