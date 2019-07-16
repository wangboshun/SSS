using SSS.Domain.CQRS.UserApi.Validations;
using SSS.Domain.UserApi.Dto;

namespace SSS.Domain.CQRS.UserApi.Command.Commands
{
    public class UserApiAddCommand : UserApiCommand
    {
        public UserApiAddCommand(UserApiInputDto input)
        {
            this.id = input.id;
            this.ApiKey = input.ApiKey;
            this.Secret = input.Secret;
            this.PassPhrase = input.PassPhrase;
            this.UserId = input.UserId;
        }

        public override bool IsValid()
        {
            ValidationResult = new UserApiAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
