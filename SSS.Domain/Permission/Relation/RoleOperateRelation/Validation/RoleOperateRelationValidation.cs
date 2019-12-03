using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.RoleOperateRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleOperateRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleOperateRelationInputDto>))]
    public class RoleOperateRelationAddValidation : AbstractValidator<RoleOperateRelationInputDto>
    {
        public RoleOperateRelationAddValidation()
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
