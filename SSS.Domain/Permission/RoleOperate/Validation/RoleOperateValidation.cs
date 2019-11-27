using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.RoleOperate.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleOperateInputDto>))]
    public class RoleOperateAddValidation : AbstractValidator<RoleOperateInputDto>
    {
        public RoleOperateAddValidation()
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
