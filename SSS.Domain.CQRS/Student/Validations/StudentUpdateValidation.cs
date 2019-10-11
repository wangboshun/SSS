using SSS.Domain.CQRS.Student.Command.Commands;

namespace SSS.Domain.CQRS.Student.Validations
{
    public class StudentUpdateValidation : StudentValidation<StudentUpdateCommand>
    {
        public StudentUpdateValidation()
        {
            ValidateId();
            ValidateName();
            ValidateAge();
        }
    }
}
