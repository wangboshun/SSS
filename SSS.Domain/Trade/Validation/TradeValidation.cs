using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Trade.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Trade.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<TradeInputDto>))]
    public class TradeAddValidation : AbstractValidator<TradeInputDto>
    {
        public TradeAddValidation()
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
