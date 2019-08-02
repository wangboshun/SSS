using SSS.Domain.CQRS.UserApi.Command.Commands;

namespace SSS.Domain.CQRS.UserApi.Validations
{
    public class UserApiUpdateValidation : UserApiValidation<UserApiUpdateCommand>
    {
        public UserApiUpdateValidation()
        {
            ValidateId();
            ValidateApiKey();
            ValidateSecret();
            ValidatePassPhrase();
            ValidateUserId();
        }
    }
}
