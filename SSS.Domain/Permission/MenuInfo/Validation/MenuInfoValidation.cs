using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.MenuInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.MenuInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<MenuInfoInputDto>))]
    public class MenuInfoAddValidation : AbstractValidator<MenuInfoInputDto>
    {
        public MenuInfoAddValidation()
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
