using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.RolePowerRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RolePowerRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RolePowerRelationInputDto>))]
    public class RolePowerRelationAddValidation : AbstractValidator<RolePowerRelationInputDto>
    {
        public RolePowerRelationAddValidation()
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
