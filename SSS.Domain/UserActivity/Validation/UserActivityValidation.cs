using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.UserActivity.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.UserActivity.Validation
{
    [DIService(ServiceLifetime.Scoped, typeof(IValidator<UserActivityInputDto>))]
    public class UserActivityValidation : AbstractValidator<UserActivityInputDto>
    {
        public UserActivityValidation()
        {
            RuleSet("Insert", () =>
            {
                RuleFor(x => x.activityid).NotEmpty().WithMessage("活动编号错误！");
                RuleFor(x => x.userid).NotEmpty().WithMessage("用户名错误！");
                RuleFor(x => x.wechatname).NotEmpty().WithMessage("微信名错误！");
            });

            RuleSet("Delete", () => { });

            RuleSet("Update", () => { });

            RuleSet("Select", () => { });
        }
    }
}