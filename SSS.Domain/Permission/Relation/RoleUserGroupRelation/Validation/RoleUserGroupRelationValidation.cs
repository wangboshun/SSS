using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.RoleUserGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleUserGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleUserGroupRelationInputDto>))]
    public class RoleUserGroupRelationAddValidation : AbstractValidator<RoleUserGroupRelationInputDto>
    {
        public RoleUserGroupRelationAddValidation()
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
