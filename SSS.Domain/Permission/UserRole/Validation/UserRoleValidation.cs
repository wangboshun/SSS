using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.UserRole.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.UserRole.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserRoleInputDto>))]
    public class UserRoleAddValidation : AbstractValidator<UserRoleInputDto>
    {
        public UserRoleAddValidation()
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
