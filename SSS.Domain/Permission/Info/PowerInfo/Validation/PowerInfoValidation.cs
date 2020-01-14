using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Info.PowerInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerInfoInputDto>))]
    public class PowerInfoAddValidation : AbstractValidator<PowerInfoInputDto>
    {
        public PowerInfoAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.powername).NotNull().WithMessage("权限名称不能为空");
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
