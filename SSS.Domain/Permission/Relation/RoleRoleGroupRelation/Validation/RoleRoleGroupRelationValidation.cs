using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleRoleGroupRelationInputDto>))]
    public class RoleRoleGroupRelationAddValidation : AbstractValidator<RoleRoleGroupRelationInputDto>
    {
        public RoleRoleGroupRelationAddValidation()
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
