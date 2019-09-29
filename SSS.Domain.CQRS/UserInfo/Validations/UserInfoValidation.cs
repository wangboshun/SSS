using FluentValidation;
using SSS.Domain.CQRS.UserInfo.Command.Commands;

namespace SSS.Domain.CQRS.UserInfo.Validations
{
    public abstract class UserInfoValidation<T> : AbstractValidator<T> where T : UserInfoCommand
    {

        protected void ValidateId()
        {
            RuleFor(c => c.id).NotEmpty().WithMessage("请输入Id");
        }

        protected void ValidateUserName()
        {
            RuleFor(c => c.username).NotEmpty().WithMessage("请输入用户名");
        }

        protected void ValidatePassWord()
        {
            RuleFor(c => c.password).NotEmpty().WithMessage("请输入密码");
        }
    }
}
