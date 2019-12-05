using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.UserGroupPowerGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserGroupPowerGroupRelationInputDto>))]
    public class UserGroupPowerGroupRelationAddValidation : AbstractValidator<UserGroupPowerGroupRelationInputDto>
    {
        public UserGroupPowerGroupRelationAddValidation()
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
