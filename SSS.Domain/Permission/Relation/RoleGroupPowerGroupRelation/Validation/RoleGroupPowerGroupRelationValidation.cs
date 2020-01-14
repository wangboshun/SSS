using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleGroupPowerGroupRelation.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<RoleGroupPowerGroupRelationInputDto>))]
    public class RoleGroupPowerGroupRelationAddValidation : AbstractValidator<RoleGroupPowerGroupRelationInputDto>
    {
        public RoleGroupPowerGroupRelationAddValidation()
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
