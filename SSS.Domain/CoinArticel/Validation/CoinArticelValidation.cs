using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Coin.CoinArticel.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Coin.CoinArticel.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<CoinArticelInputDto>))]
    public class CoinArticelValidation : AbstractValidator<CoinArticelInputDto>
    {
        public CoinArticelValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.id).NotNull().WithMessage("ID不能为空");
            });

            RuleSet("Delete", () =>
            {

            });

            RuleSet("Update", () =>
            {

            });

            RuleSet("Select", () => { });
        }
    }
}