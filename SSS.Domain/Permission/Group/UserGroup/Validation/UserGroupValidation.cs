using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Group.UserGroup.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserGroupInputDto>))]
    public class UserGroupAddValidation : AbstractValidator<UserGroupInputDto>
    {
        public UserGroupAddValidation()
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
