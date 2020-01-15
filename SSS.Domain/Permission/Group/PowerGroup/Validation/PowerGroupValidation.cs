using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Group.PowerGroup.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerGroupInputDto>))]
    public class PowerGroupAddValidation : AbstractValidator<PowerGroupInputDto>
    {
        public PowerGroupAddValidation()
        {
            RuleSet("Insert", () => { RuleFor(x => x.powergroupname).NotNull().WithMessage("权限组名称不能为空!"); });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}