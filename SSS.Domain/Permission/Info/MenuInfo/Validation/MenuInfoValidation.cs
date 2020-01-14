using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Info.MenuInfo.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<MenuInfoInputDto>))]
    public class MenuInfoAddValidation : AbstractValidator<MenuInfoInputDto>
    {
        public MenuInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.menuname).NotEmpty().WithMessage("�˵����Ʋ���Ϊ�գ�");
            });

            RuleSet("Delete", () =>
            {

            });

            RuleSet("Update", () =>
            {
                RuleFor(x => x.menuname).NotEmpty().WithMessage("�˵����Ʋ���Ϊ�գ�");
                RuleFor(x => x.id).NotEmpty().WithMessage("Id����Ϊ�գ�");
            });

            RuleSet("Select", () =>
            {

            });
        }
    }
}
