using SSS.Domain.CQRS.Articel.Validations;
using SSS.Domain.Articel.Dto;

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
            sort = input.sort;
            issmain = input.issmain;
            contenttype = input.contenttype;
        }

        public override bool IsValid()
        {
            ValidationResult = new ArticelAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
