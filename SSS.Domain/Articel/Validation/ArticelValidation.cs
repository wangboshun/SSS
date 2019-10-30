using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Articel.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Articel.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<ArticelInputDto>))]
    public class ArticelValidation : AbstractValidator<ArticelInputDto>
    {
        public ArticelValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.id).NotNull().WithMessage("ID不能为空");
            });

            RuleSet("Delete", () =>
            {

            });

            RuleSet("Update", () =>
            {

            });

            RuleSet("Select", () => { });
        }
    }
}