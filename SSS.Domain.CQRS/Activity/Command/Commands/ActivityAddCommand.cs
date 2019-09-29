using SSS.Domain.Activity.Dto;
using SSS.Domain.CQRS.Activity.Validations;

namespace SSS.Domain.CQRS.Activity.Command.Commands
{
    public class ActivityAddCommand : ActivityCommand
    {
        public ActivityAddCommand(ActivityInputDto input)
        {
            this.id = input.id;
        }

        public override bool IsValid()
        {
            ValidationResult = new ActivityAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
