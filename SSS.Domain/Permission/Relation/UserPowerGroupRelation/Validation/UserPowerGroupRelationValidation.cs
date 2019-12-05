using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserPowerGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.UserPowerGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserPowerGroupRelationInputDto>))]
    public class UserPowerGroupRelationAddValidation : AbstractValidator<UserPowerGroupRelationInputDto>
    {
        public UserPowerGroupRelationAddValidation()
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
