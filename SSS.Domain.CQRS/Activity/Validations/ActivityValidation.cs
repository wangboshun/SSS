using FluentValidation;
using SSS.Domain.CQRS.Activity.Command.Commands;
using System;

namespace SSS.Domain.CQRS.Activity.Validations
{
    public abstract class ActivityValidation<T> : AbstractValidator<T> where T : ActivityCommand
    {

        protected void ValidateId()
        {
             RuleFor(c => c.id).NotEmpty().WithMessage("请输入Id");
        }
    }
}
