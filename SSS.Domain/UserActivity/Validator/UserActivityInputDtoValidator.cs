using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.UserActivity.Dto;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Domain.UserActivity.Validator
{
    [DIService(ServiceLifetime.Transient, typeof(IValidator<UserActivityInputDto>))]
    public class UserActivityInputDtoValidator : AbstractValidator<UserActivityInputDto>
    {
        public UserActivityInputDtoValidator()
        {
            RuleFor(x => x.order_by).NotNull().WithMessage("不为空");
        }
    }
}
