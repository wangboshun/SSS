using SSS.Domain.CQRS.Student.Command.Commands;

namespace SSS.Domain.CQRS.Student.Validations
{
    public class StudentAddValidation : StudentValidation<StudentAddCommand>
    {
        public StudentAddValidation()
        {
            ValidateId();
            ValidateName();
            ValidateAge();
        }
    }
}
