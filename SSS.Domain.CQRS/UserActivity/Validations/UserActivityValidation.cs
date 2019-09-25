using FluentValidation;
using SSS.Domain.CQRS.UserActivity.Command.Commands;
using System;

namespace SSS.Domain.CQRS.UserActivity.Validations
{
    public abstract class UserActivityValidation<T> : AbstractValidator<T> where T : UserActivityCommand
    {

        protected void ValidateId()
        {
             RuleFor(c => c.id).NotEmpty().WithMessage("请输入Id");
        }
    }
}
