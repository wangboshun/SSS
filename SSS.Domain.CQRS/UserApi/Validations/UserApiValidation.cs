using FluentValidation;
using SSS.Domain.CQRS.UserApi.Command.Commands;
using System;

namespace SSS.Domain.CQRS.UserApi.Validations
{
    public abstract class UserApiValidation<T> : AbstractValidator<T> where T : UserApiCommand
    {

        protected void ValidateId()
        {
             RuleFor(c => c.id).NotEmpty().WithMessage("请输入Id");
        }

        protected void ValidateApiKey()
        {
            RuleFor(c => c.ApiKey).NotEmpty().WithMessage("请输入ApiKey");
        }

        protected void ValidateSecret()
        {
            RuleFor(c => c.Secret).NotEmpty().WithMessage("请输入Secret");
        }

        protected void ValidatePassPhrase()
        {
            RuleFor(c => c.PassPhrase).NotEmpty().WithMessage("请输入PassPhrase");
        }

        protected void ValidateUserId()
        {
            RuleFor(c => c.UserId).NotEmpty().WithMessage("请输入UserId");
        }
    }
}
