using SSS.Domain.CQRS.UserActivity.Validations;
using SSS.Domain.UserActivity.Dto;

namespace SSS.Domain.CQRS.UserActivity.Command.Commands
{
    public class UserActivityAddCommand : UserActivityCommand
    {
        public UserActivityAddCommand(UserActivityInputDto input)
        {
            this.id = input.id;
            this.activityid = input.activityid;
            this.grouptotal = input.grouptotal;
            this.userid = input.userid;
            this.wechatname = input.wechatname;
        }

        public override bool IsValid()
        {
            ValidationResult = new UserActivityAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
