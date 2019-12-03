using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Relation.UserUserGroupRelation.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserUserGroupRelationInputDto>))]
    public class UserUserGroupRelationAddValidation : AbstractValidator<UserUserGroupRelationInputDto>
    {
        public UserUserGroupRelationAddValidation()
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
