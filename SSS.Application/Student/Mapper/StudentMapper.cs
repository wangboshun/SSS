using SSS.Domain.Student.Dto;

namespace SSS.Application.Student.Profile
{
    public class StudentMapper : AutoMapper.Profile
    {
        public StudentMapper()
        {
            CreateMap<Domain.Student.Student, StudentOutputDto>();

            CreateMap<StudentInputDto, SSS.Domain.Student.Student>();
        }
    }
}
