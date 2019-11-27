using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.OperateInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.OperateInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<OperateInfoInputDto>))]
    public class OperateInfoAddValidation : AbstractValidator<OperateInfoInputDto>
    {
        public OperateInfoAddValidation()
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
