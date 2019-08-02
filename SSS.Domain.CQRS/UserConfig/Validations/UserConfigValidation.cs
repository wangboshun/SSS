using FluentValidation;
using SSS.Domain.CQRS.UserConfig.Command.Commands;

namespace SSS.Domain.CQRS.UserConfig.Validations
{
    public abstract class UserConfigValidation<T> : AbstractValidator<T> where T : UserConfigCommand
    {

        protected void ValidateId()
        {
            RuleFor(c => c.id).NotEmpty().WithMessage("请输入Id");
        }
    }
}
