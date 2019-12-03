using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Group.RoleGroup.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleGroupInputDto>))]
    public class RoleGroupAddValidation : AbstractValidator<RoleGroupInputDto>
    {
        public RoleGroupAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.rolegroupname).NotNull().WithMessage("角色组名称不能为空!");
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
