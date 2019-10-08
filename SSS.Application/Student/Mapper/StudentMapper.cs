using SSS.Domain.CQRS.Student.Command.Commands;
using SSS.Domain.Student.Dto;

namespace SSS.Application.Student.Profile
{
    public class StudentMapper : AutoMapper.Profile
    {
        public StudentMapper()
        {
            CreateMap<Domain.Student.Student, StudentOutputDto>();

            CreateMap<StudentInputDto, SSS.Domain.Student.Student>();

            CreateMap<StudentInputDto, StudentUpdateCommand>()
               .ConstructUsing(input => new StudentUpdateCommand(input));

            CreateMap<StudentInputDto, StudentAddCommand>()
                .ConstructUsing(input => new StudentAddCommand(input));
        }
    }
}
