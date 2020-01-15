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
                RuleFor(x => x.usergroupid).NotEmpty().WithMessage("用户组不能为空！");
                RuleFor(x => x.userid).NotEmpty().WithMessage("用户不能为空！");
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