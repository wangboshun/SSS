using SSS.Domain.CQRS.Activity.Command.Commands;

namespace SSS.Domain.CQRS.Activity.Validations
{
    public class ActivityAddValidation : ActivityValidation<ActivityAddCommand>
    {
        public ActivityAddValidation()
        {
            ValidateId();
        }
    }
}
