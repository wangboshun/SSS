using FluentValidation;
using SSS.Domain.CQRS.Articel.Command.Commands;

namespace SSS.Domain.CQRS.Articel.Validations
{
    public abstract class ArticelValidation<T> : AbstractValidator<T> where T : ArticelCommand
    {

        protected void ValidateId()
        {
            RuleFor(c => c.id).NotEmpty().WithMessage("请输入Id");
        }
    }
}
