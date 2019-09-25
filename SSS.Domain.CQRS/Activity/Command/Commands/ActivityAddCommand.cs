using SSS.Domain.CQRS.Activity.Validations;
using SSS.Domain.Activity.Dto;

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
