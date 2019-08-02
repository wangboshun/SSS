using SSS.Domain.CQRS.UserInfo.Validations;
using SSS.Domain.UserInfo.Dto;

namespace SSS.Domain.CQRS.UserInfo.Command.Commands
{
    public class UserInfoAddCommand : UserInfoCommand
    {
        public UserInfoAddCommand(UserInfoInputDto input)
        {
            this.id = input.id;
            this.name = input.name;
            this.phone = input.phone;
            this.openid = input.openid;
        }

        public override bool IsValid()
        {
            ValidationResult = new UserInfoAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
