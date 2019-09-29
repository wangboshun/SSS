using SSS.Domain.Articel.Dto;
using SSS.Domain.CQRS.Articel.Validations;

namespace SSS.Domain.CQRS.Articel.Command.Commands
{
    public class ArticelAddCommand : ArticelCommand
    {
        public ArticelAddCommand(ArticelInputDto input)
        {
            this.id = input.id;
        }

        public override bool IsValid()
        {
            ValidationResult = new ArticelAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
