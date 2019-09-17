using SSS.Domain.CQRS.Articel.Command.Commands;

namespace SSS.Domain.CQRS.Articel.Validations
{
    public class ArticelAddValidation : ArticelValidation<ArticelAddCommand>
    {
        public ArticelAddValidation()
        {
            ValidateId();
        }
    }
}
