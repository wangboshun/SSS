using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.DigitalCurrency.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.DigitalCurrency.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<DigitalCurrencyInputDto>))]
    public class DigitalCurrencyAddValidation : AbstractValidator<DigitalCurrencyInputDto>
    {
        public DigitalCurrencyAddValidation()
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
