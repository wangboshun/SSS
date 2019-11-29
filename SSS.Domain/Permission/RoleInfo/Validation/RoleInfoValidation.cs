using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.RoleInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleInfoInputDto>))]
    public class RoleInfoAddValidation : AbstractValidator<RoleInfoInputDto>
    {
        public RoleInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.rolename).NotEmpty().WithMessage("角色名称不能为空！");
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
