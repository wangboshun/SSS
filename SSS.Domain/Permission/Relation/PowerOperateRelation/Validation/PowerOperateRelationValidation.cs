using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.PowerOperateRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerOperateRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<PowerOperateRelationInputDto>))]
    public class PowerOperateRelationAddValidation : AbstractValidator<PowerOperateRelationInputDto>
    {
        public PowerOperateRelationAddValidation()
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
