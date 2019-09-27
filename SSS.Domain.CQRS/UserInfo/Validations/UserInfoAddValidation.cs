using SSS.Domain.CQRS.UserInfo.Command.Commands;

namespace SSS.Domain.CQRS.UserInfo.Validations
{
    public class UserInfoAddValidation : UserInfoValidation<UserInfoAddCommand>
    {
        public UserInfoAddValidation()
        {
            ValidateId();
        }
    }
}
