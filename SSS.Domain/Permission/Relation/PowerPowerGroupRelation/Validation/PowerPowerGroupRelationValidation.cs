using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerPowerGroupRelationInputDto>))]
    public class PowerPowerGroupRelationAddValidation : AbstractValidator<PowerPowerGroupRelationInputDto>
    {
        public PowerPowerGroupRelationAddValidation()
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
