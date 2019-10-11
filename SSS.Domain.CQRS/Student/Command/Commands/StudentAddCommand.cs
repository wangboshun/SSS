using SSS.Domain.CQRS.Student.Validations;
using SSS.Domain.Student.Dto;

namespace SSS.Domain.CQRS.Student.Command.Commands
{
    public class StudentAddCommand : StudentCommand
    {
        public StudentAddCommand(StudentInputDto input)
        {
            this.id = input.id;
            this.name = input.name;
            this.age = input.age;
        }

        public override bool IsValid()
        {
            ValidationResult = new StudentAddValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
