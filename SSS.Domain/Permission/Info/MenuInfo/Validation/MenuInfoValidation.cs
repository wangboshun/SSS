using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Info.MenuInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<MenuInfoInputDto>))]
    public class MenuInfoAddValidation : AbstractValidator<MenuInfoInputDto>
    {
        public MenuInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.menuname).NotEmpty().WithMessage("菜单名称不能为空！");
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
