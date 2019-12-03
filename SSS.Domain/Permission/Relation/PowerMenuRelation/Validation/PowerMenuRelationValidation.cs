using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerMenuRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerMenuRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerMenuRelationInputDto>))]
    public class PowerMenuRelationAddValidation : AbstractValidator<PowerMenuRelationInputDto>
    {
        public PowerMenuRelationAddValidation()
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
