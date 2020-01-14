using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleGroupRelation.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<RoleGroupRelationInputDto>))]
    public class RoleGroupRelationAddValidation : AbstractValidator<RoleGroupRelationInputDto>
    {
        public RoleGroupRelationAddValidation()
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
