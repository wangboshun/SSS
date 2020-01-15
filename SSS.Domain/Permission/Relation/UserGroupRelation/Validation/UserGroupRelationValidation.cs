using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.UserGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserGroupRelationInputDto>))]
    public class UserGroupRelationAddValidation : AbstractValidator<UserGroupRelationInputDto>
    {
        public UserGroupRelationAddValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.usergroupid).NotEmpty().WithMessage("�û��鲻��Ϊ�գ�");
                RuleFor(x => x.userid).NotEmpty().WithMessage("�û�����Ϊ�գ�");
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