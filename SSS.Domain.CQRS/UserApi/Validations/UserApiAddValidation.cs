using SSS.Domain.CQRS.UserApi.Command.Commands;

namespace SSS.Domain.CQRS.UserApi.Validations
{
    public class UserApiAddValidation : UserApiValidation<UserApiAddCommand>
    {
        public UserApiAddValidation()
        {
            ValidateId();
            ValidateApiKey();
            ValidateSecret();
            ValidatePassPhrase();
            ValidateUserId();
        }
    }
}
