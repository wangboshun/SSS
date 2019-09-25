using SSS.Domain.CQRS.UserActivity.Validations;
using SSS.Domain.UserActivity.Dto;

namespace SSS.Domain.CQRS.UserActivity.Command.Commands
{
    public class UserActivityAddCommand : UserActivityCommand
    {
        public UserActivityAddCommand(UserActivityInputDto input)
        {
            this.id = input.id; 
        }

        public override bool IsValid()
        {
            ValidationResult = new UserActivityAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
