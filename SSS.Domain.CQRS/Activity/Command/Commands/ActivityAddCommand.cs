using SSS.Domain.Activity.Dto;
using SSS.Domain.CQRS.Activity.Validations;

namespace SSS.Domain.CQRS.Activity.Command.Commands
{
    public class ActivityAddCommand : ActivityCommand
    {
        public ActivityAddCommand(ActivityInputDto input)
        {
            inputDto = input;
        }

        public override bool IsValid()
        {
            ValidationResult = new ActivityAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
