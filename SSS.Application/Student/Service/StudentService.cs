using System;
using System.Collections.Generic;
using System.Linq;
using AutoMapper;
using AutoMapper.QueryableExtensions;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.CQRS.Student.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Student.Dto;
using SSS.Infrastructure.Repository.Student;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Application.Student.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IStudentService))]
    public class StudentService : IStudentService
    {
        [Autowired]
        private readonly IMapper _mapper;

        [Autowired]
        private readonly IEventBus _bus;

        [Autowired]
        private readonly IStudentRepository _studentrepository;

        public void AddStudent(StudentInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<StudentAddCommand>(input);
            _bus.SendCommand(cmd);
        }
        public void DeleteStudent(StudentInputDto student) => throw new System.NotImplementedException();
        public StudentOutputDto GetByName(StudentInputDto student)
        {
            return _mapper.Map<StudentOutputDto>(_studentrepository.GetByName(student.name));
        }
        public Pages<List<StudentOutputDto>> GetListStudent(StudentInputDto input)
        {
            List<StudentOutputDto> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                var temp = _studentrepository.GetAll();
                list = _studentrepository.GetAll().ProjectTo<StudentOutputDto>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _studentrepository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<StudentOutputDto>(_mapper.ConfigurationProvider).ToList();

            return new Pages<List<StudentOutputDto>>(list, count);
        }
        public void UpdateStudent(StudentInputDto input)
        {
            var cmd = _mapper.Map<StudentUpdateCommand>(input);
            _bus.SendCommand(cmd);
        }
    }
}