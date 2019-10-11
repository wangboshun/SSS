using SSS.Domain.CQRS.UserInfo.Validations;
using SSS.Domain.UserInfo.Dto;

namespace SSS.Domain.CQRS.UserInfo.Command.Commands
{
    public class UserInfoAddCommand : UserInfoCommand
    {
        public UserInfoAddCommand(UserInfoInputDto input)
        {
            inputDto = input;
        }

        public override bool IsValid()
        {
            ValidationResult = new UserInfoAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
