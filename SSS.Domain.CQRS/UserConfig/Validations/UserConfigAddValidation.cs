using SSS.Domain.CQRS.UserConfig.Command.Commands;

namespace SSS.Domain.CQRS.UserConfig.Validations
{
    public class UserConfigAddValidation : UserConfigValidation<UserConfigAddCommand>
    {
        public UserConfigAddValidation()
        {
            ValidateId();
        }
    }
}
