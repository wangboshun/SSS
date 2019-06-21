using SSS.Domain.Articel.Dto;
using SSS.Domain.CQRS.Articel.Validations;

namespace SSS.Domain.CQRS.Articel.Command.Commands
{
    public class ArticelAddCommand : ArticelCommand
    {
        public ArticelAddCommand(ArticelInputDto input)
        {
            id = input.id;
            title = input.title;
            content = input.content;
            mainimage = input.mainimage;
            sort = (int)input.sort;
            issmain = (int)input.issmain;
            contenttype = (int)input.contenttype;
        }

        public override bool IsValid()
        {
            ValidationResult = new ArticelAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
