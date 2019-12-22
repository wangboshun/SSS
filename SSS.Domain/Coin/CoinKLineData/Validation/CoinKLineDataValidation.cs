using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Coin.CoinKLineData.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Coin.CoinKLineData.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CoinKLineDataInputDto>))]
    public class CoinKLineDataAddValidation : AbstractValidator<CoinKLineDataInputDto>
    {
        public CoinKLineDataAddValidation()
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
