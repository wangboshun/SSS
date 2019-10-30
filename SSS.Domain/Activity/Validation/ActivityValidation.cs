using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Activity.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Activity.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<ActivityInputDto>))]
    public class ActivityValidation : AbstractValidator<ActivityInputDto>
    {
        public ActivityValidation()
        {
            RuleSet("Insert", () => { });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}