using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Student.Dto;
using System.Collections.Generic;

namespace SSS.Application.Student.Service
{
    public interface IStudentService : IQueryService<SSS.Domain.Student.Student, StudentInputDto, StudentOutputDto>
    {
        void AddStudent(StudentInputDto input);

        void UpdateStudent(StudentInputDto input);

        Pages<List<StudentOutputDto>> GetListStudent(StudentInputDto input);

        void DeleteStudent(StudentInputDto input);

        StudentOutputDto GetByName(StudentInputDto input);
    }
}