using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.Permission.Info.UserInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserInfoInputDto>))]
    public class UserInfoValidation : AbstractValidator<UserInfoInputDto>
    {
        public UserInfoValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.username).NotEmpty().WithMessage("用户名不能为空！");
                RuleFor(x => x.password).NotEmpty().WithMessage("密码不能为空！");
            });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}