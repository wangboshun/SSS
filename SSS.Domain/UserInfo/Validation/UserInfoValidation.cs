using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.UserInfo.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserInfoInputDto>))]
    public class UserInfoValidation : AbstractValidator<UserInfoInputDto>
    {
        public UserInfoValidation()
        {
            RuleSet("Insert", () => { });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}