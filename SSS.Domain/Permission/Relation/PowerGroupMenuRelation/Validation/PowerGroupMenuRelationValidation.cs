using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerGroupMenuRelationInputDto>))]
    public class PowerGroupMenuRelationAddValidation : AbstractValidator<PowerGroupMenuRelationInputDto>
    {
        public PowerGroupMenuRelationAddValidation()
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
