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
                RuleFor(x => x.userid).NotEmpty().WithMessage("用户不能为空！");
                RuleFor(x => x.roleid).NotEmpty().WithMessage("角色不能为空！");
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
