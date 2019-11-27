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
