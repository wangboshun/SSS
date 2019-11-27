using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domai.Permission.RoleMenu.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleMenuInputDto>))]
    public class RoleMenuAddValidation : AbstractValidator<RoleMenuInputDto>
    {
        public RoleMenuAddValidation()
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
