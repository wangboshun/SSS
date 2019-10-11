using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Student.Dto;
using SSS.Infrastructure.Repository.Student;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Student.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IStudentService))]
    public class StudentService : QueryService<SSS.Domain.Student.Student, StudentInputDto, StudentOutputDto>, IStudentService
    {
        private readonly IMapper _mapper;
        private readonly IStudentRepository _repository;
        private readonly MemoryCacheEx _memorycache;
        private readonly ILogger _logger;

        public StudentService(IMapper mapper, MemoryCacheEx memorycache, IStudentRepository repository, ILogger<StudentService> logger) : base(mapper, repository)
        {
            _mapper = mapper;
            _repository = repository;
            _memorycache = memorycache;
            _logger = logger;
        }

        public void AddStudent(StudentInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
        }
        public void DeleteStudent(StudentInputDto student) => throw new System.NotImplementedException();
        public StudentOutputDto GetByName(StudentInputDto student)
        {
            return _mapper.Map<StudentOutputDto>(_repository.GetByName(student.name));
        }
        public Pages<List<StudentOutputDto>> GetListStudent(StudentInputDto input)
        {
            return GetList(input);
        }
        public void UpdateStudent(StudentInputDto input)
        {
        }
    }
}