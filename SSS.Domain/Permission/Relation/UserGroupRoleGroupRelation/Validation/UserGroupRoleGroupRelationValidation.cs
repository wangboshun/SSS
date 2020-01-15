using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.UserGroupRoleGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserGroupRoleGroupRelationInputDto>))]
    public class UserGroupRoleGroupRelationAddValidation : AbstractValidator<UserGroupRoleGroupRelationInputDto>
    {
        public UserGroupRoleGroupRelationAddValidation()
        {
            RuleSet("Insert", () => { });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}