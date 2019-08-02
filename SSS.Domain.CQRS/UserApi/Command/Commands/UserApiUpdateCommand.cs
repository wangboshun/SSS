using SSS.Domain.CQRS.UserApi.Validations;
using SSS.Domain.UserApi.Dto;

namespace SSS.Domain.CQRS.UserApi.Command.Commands
{
    public class UserApiUpdateCommand : UserApiCommand
    {
        public UserApiUpdateCommand(UserApiInputDto input)
        {
            this.id = input.id;
            this.ApiKey = input.ApiKey;
            this.Secret = input.Secret;
            this.PassPhrase = input.PassPhrase;
            this.UserId = input.UserId;
        }

        public override bool IsValid()
        {
            ValidationResult = new UserApiUpdateValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
