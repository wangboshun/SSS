using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Community.UserCommunityRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Community.UserCommunityRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserCommunityRelationInputDto>))]
    public class UserCommunityRelationAddValidation : AbstractValidator<UserCommunityRelationInputDto>
    {
        public UserCommunityRelationAddValidation()
        {
            RuleSet("Insert", () => { });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}