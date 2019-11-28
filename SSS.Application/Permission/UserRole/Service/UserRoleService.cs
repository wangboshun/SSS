using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.UserRole;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.UserRole.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserRoleService))]
    public class UserRoleService : QueryService<SSS.Domain.Permission.UserRole.UserRole, UserRoleInputDto, UserRoleOutputDto>, IUserRoleService
    {
        private readonly IUserRoleRepository _repository;

        public UserRoleService(IMapper mapper,
            IUserRoleRepository repository,
            IErrorHandler error,
            IValidator<UserRoleInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
        }

        public void AddUserRole(UserRoleInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.UserRole.UserRole>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<UserRoleOutputDto>> GetListUserRole(UserRoleInputDto input)
        {
            return GetPage(input);
        }

        public List<UserRoleOutputDto> GetUserByRole(string roleid)
        {
            return _repository.GetUserByRole(roleid);
        }
    }
}