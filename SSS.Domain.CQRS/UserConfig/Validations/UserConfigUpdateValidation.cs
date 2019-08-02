using SSS.Domain.CQRS.UserConfig.Command.Commands;

namespace SSS.Domain.CQRS.UserConfig.Validations
{
    public class UserConfigUpdateValidation : UserConfigValidation<UserConfigUpdateCommand>
    {
        public UserConfigUpdateValidation()
        {
            ValidateId();
        }
    }
}
