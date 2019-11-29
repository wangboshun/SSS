using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.RoleMenu;
using SSS.Infrastructure.Repository.Permission.RoleOperate;
using SSS.Infrastructure.Repository.Permission.UserInfo;
using SSS.Infrastructure.Repository.Permission.UserRole;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService : QueryService<Domain.Permission.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>,
        IUserInfoService
    {
        private readonly MemoryCacheEx _memorycache;

        private readonly IUserRoleRepository _userroleRepository;
        private readonly IUserInfoRepository _userinfoRepository;
        private readonly IRoleMenuRepository _rolemenuRepository;
        private readonly IRoleOperateRepository _roleoperateRepository;

        public UserInfoService(IMapper mapper,
            IUserInfoRepository repository,
            IErrorHandler error,
            IValidator<UserInfoInputDto> validator,
            MemoryCacheEx memorycache,
            IUserRoleRepository userroleRepository,
            IRoleMenuRepository rolemenuRepository,
            IRoleOperateRepository roleoperateRepository
            ) : base(mapper, repository, error, validator)
        {
            _memorycache = memorycache;
            _userinfoRepository = repository;
            _userroleRepository = userroleRepository;
            _rolemenuRepository = rolemenuRepository;
            _roleoperateRepository = roleoperateRepository;
        }

        /// <summary>
        /// 获取用户下所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public object GetUserPermission(string userid)
        {
            var role = _userroleRepository.GetRoleByUser(userid);
            if (role == null)
                return null;

            var menu = _rolemenuRepository.GetRoleMenuByRole(role?.Id);
            var operate = _roleoperateRepository.GetRoleOperateByRole(role?.Id);

            return new { menu, operate };
        }

        /// <summary>
        /// 删除用户权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public object DeleteUserPermission(string userid)
        {
            var role = _userroleRepository.GetRoleByUser(userid);
            if (role == null)
                return null;

            var menu = _rolemenuRepository.GetRoleMenuByRole(role.Id);
            var operate = _roleoperateRepository.GetRoleOperateByRole(role.Id);

            return new { menu, operate };
        }

        /// <summary>
        /// 获取用户下的所有下级
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public List<UserInfoTreeOutputDto> GetChildrenById(string userid)
        {
            return _userinfoRepository.GetChildrenById(userid);
        }

        /// <summary>
        /// 添加用户
        /// </summary>
        /// <param name="input"></param>
        public void AddUserInfo(UserInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var user = Get(x => x.UserName.Equals(input.username));
            if (user != null)
            {
                Error.Execute("用户已存在！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.UserInfo.UserInfo>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model, true);
        }

        public void DeleteUserInfo(UserInfoInputDto input)
        {
            Delete(input.id);
        }

        /// <summary>
        /// 账号密码登录
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public UserInfoOutputDto GetByUserName(UserInfoInputDto input)
        {
            var result = Get(x => x.UserName.Equals(input.username) && x.PassWord.Equals(input.password));
            if (result == null)
            {
                Error.Execute("账户密码错误！");
                return null;
            }

            var userinfo = Mapper.Map<UserInfoOutputDto>(result);
            _memorycache.Set("AuthUserInfo_" + userinfo.id, userinfo, 60 * 24);
            return userinfo;
        }

        public Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}