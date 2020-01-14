using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerGroupRelation.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<PowerGroupRelationInputDto>))]
    public class PowerGroupRelationAddValidation : AbstractValidator<PowerGroupRelationInputDto>
    {
        public PowerGroupRelationAddValidation()
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
