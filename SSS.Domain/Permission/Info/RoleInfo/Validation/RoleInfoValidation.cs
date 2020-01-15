using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Info.RoleInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleInfoInputDto>))]
    public class RoleInfoAddValidation : AbstractValidator<RoleInfoInputDto>
    {
        public RoleInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.rolename).NotEmpty().WithMessage("��ɫ���Ʋ���Ϊ�գ�");
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