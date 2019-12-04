using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerGroupMenuRelationInputDto>))]
    public class PowerGroupMenuRelationAddValidation : AbstractValidator<PowerGroupMenuRelationInputDto>
    {
        public PowerGroupMenuRelationAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.powerid).NotNull().WithMessage("权限Id不能为空");
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
