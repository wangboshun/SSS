using SSS.Domain.CQRS.Student.Validations;
using SSS.Domain.Student.Dto;

namespace SSS.Domain.CQRS.Student.Command.Commands
{
    public class StudentUpdateCommand : StudentCommand
    {
        public StudentUpdateCommand(StudentInputDto input)
        {
            this.id = input.id;
            this.name = input.name;
            this.age = input.age;
        }

        public override bool IsValid()
        {
            ValidationResult = new StudentUpdateValidation().Validate(this);
            return ValidationResult.IsValid;
        }
    }
}
