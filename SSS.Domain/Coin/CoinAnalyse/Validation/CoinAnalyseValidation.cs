using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinAnalyse.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Coin.CoinAnalyse.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CoinAnalyseInputDto>))]
    public class CoinAnalyseAddValidation : AbstractValidator<CoinAnalyseInputDto>
    {
        public CoinAnalyseAddValidation()
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
