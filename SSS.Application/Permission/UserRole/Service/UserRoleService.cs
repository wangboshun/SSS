using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserRole.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.RoleInfo;
using SSS.Infrastructure.Repository.Permission.UserInfo;
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
        private readonly IUserInfoRepository _userInfoRepository;
        private readonly IRoleInfoRepository _roleInfoRepository;

        public UserRoleService(IMapper mapper,
            IUserRoleRepository repository,
            IErrorHandler error,
            IValidator<UserRoleInputDto> validator,
            IUserInfoRepository userInfoRepository,
            IRoleInfoRepository roleInfoRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _userInfoRepository = userInfoRepository;
            _roleInfoRepository = roleInfoRepository;
        }

        public void AddUserRole(UserRoleInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var user = _userInfoRepository.Get(input.userid);
            if (user == null)
            {
                Error.Execute("用户错误！");
                return;
            }

            var role = _roleInfoRepository.Get(input.roleid);
            if (role == null)
            {
                Error.Execute("用户错误！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.UserRole.UserRole>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<UserRoleOutputDto>> GetListUserRole(UserRoleInputDto input)
        {
            return GetPage(input);
        }

        /// <summary>
        /// 获取角色下所有用户信息
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<UserRoleOutputDto> GetUserByRole(string roleid)
        {
            return _repository.GetUserRoleByRole(roleid);
        }
        public void DeleteUserRole(UserRoleInputDto input)
        {
            Delete(input.id);
        }

        /// <summary>
        /// 删除角色下的所有用户
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public bool DeleteUserRoleByRole(string roleid)
        {
            return _repository.DeleteUserRoleByRole(roleid);
        }
    }
}