using SSS.Domain.CQRS.UserConfig.Validations;
using SSS.Domain.UserConfig.Dto;

namespace SSS.Domain.CQRS.UserConfig.Command.Commands
{
    public class UserConfigAddCommand : UserConfigCommand
    {
        public UserConfigAddCommand(UserConfigInputDto input)
        {
            this.id = input.id; 
        }

        public override bool IsValid()
        {
            ValidationResult = new UserConfigAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
