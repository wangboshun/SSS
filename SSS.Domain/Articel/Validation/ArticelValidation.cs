using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Articel.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.UserActivity.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<ArticelInputDto>))]
    public class ArticelValidation : AbstractValidator<ArticelInputDto>
    {
        public ArticelValidation()
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
