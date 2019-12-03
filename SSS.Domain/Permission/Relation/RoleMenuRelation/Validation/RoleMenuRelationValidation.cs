using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.RoleMenuRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleMenuRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleMenuRelationInputDto>))]
    public class RoleMenuRelationAddValidation : AbstractValidator<RoleMenuRelationInputDto>
    {
        public RoleMenuRelationAddValidation()
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
