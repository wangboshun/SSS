using SSS.Domain.CQRS.UserConfig.Validations;
using SSS.Domain.UserConfig.Dto;

namespace SSS.Domain.CQRS.UserConfig.Command.Commands
{
    public class UserConfigUpdateCommand : UserConfigCommand
    {
        public UserConfigUpdateCommand(UserConfigInputDto input)
        {
            this.id = input.id;
            this.coin = input.coin;
            this.size = input.size;
            this.userid = input.UserId;
            this.profit = input.profit;
            this.loss = input.loss;
            this.ktime = input.ktime;
            this.userid = input.UserId;
        }

        public override bool IsValid()
        {
            ValidationResult = new UserConfigUpdateValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
