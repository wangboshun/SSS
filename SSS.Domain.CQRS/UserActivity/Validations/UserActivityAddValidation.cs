using SSS.Domain.CQRS.UserActivity.Command.Commands;

namespace SSS.Domain.CQRS.UserActivity.Validations
{
    public class UserActivityAddValidation : UserActivityValidation<UserActivityAddCommand>
    {
        public UserActivityAddValidation()
        {
            ValidateId();
        }
    }
}
