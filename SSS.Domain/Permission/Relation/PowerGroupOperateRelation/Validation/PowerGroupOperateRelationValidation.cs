using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Validation
{
    [DIService(ServiceLifetime.Singleton, typeof(IValidator<PowerGroupOperateRelationInputDto>))]
    public class PowerGroupOperateRelationAddValidation : AbstractValidator<PowerGroupOperateRelationInputDto>
    {
        public PowerGroupOperateRelationAddValidation()
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
