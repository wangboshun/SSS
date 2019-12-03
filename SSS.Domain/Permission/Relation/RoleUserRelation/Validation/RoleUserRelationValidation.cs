using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.RoleUserRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.RoleUserRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<RoleUserRelationInputDto>))]
    public class RoleUserRelationAddValidation : AbstractValidator<RoleUserRelationInputDto>
    {
        public RoleUserRelationAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.userid).NotEmpty().WithMessage("�û�����Ϊ�գ�");
                RuleFor(x => x.roleid).NotEmpty().WithMessage("��ɫ����Ϊ�գ�");
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
