using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.UserGroup;
using SSS.Infrastructure.Repository.Permission.Info.UserInfo;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Info.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService :
        QueryService<Domain.Permission.Info.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>,
        IUserInfoService
    {
        private readonly MemoryCacheEx _memorycache;
        private readonly IUserInfoRepository _userinfoRepository;
        private readonly IUserGroupRepository _userGroupRepository;
        private readonly IUserGroupRelationRepository _userGroupRelationRepository;

        public UserInfoService(IMapper mapper,
            IUserInfoRepository repository,
            IErrorHandler error,
            IValidator<UserInfoInputDto> validator,
            MemoryCacheEx memorycache,
            IUserGroupRepository userGroupRepository,
            IUserGroupRelationRepository userGroupRelationRepository
        ) : base(mapper, repository, error, validator)
        {
            _memorycache = memorycache;
            _userinfoRepository = repository;
            _userGroupRepository = userGroupRepository;
            _userGroupRelationRepository = userGroupRelationRepository;
        }

        /// <summary>
        ///     获取用户下所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public object GetUserPermission(string userid)
        {
            var user = _userinfoRepository.Get(userid);
            var usergroup = "";

            var usergroup_powergroup = "";

            var menu = "";
            var powermenu = "";

            var operate = "";
            var operategroup = "";

            var role = "";
            var rolegroup = "";

            return new { menu, powermenu, operate, operategroup, role, rolegroup };
        }

        /// <summary>
        ///     删除用户所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public bool DeleteUserPermission(string userid)
        {
            return true;
        }

        /// <summary>
        ///     获取用户下的所有下级
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public List<UserInfoTreeOutputDto> GetChildrenById(string userid)
        {
            return _userinfoRepository.GetChildrenById(userid);
        }

        /// <summary>
        ///     添加用户
        /// </summary>
        /// <param name="input"></param>
        public UserInfoOutputDto AddUserInfo(UserInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            var user = Get(x => x.UserName.Equals(input.username));
            if (user != null)
            {
                Error.Execute("用户已存在！");
                return null;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.UserInfo.UserInfo>(input);
            model.CreateTime = DateTime.Now;

            if (!string.IsNullOrWhiteSpace(input.usergroupid))
            {
                var usergroup = _userGroupRepository.Get(x => x.Id.Equals(input.usergroupid));
                if (usergroup != null)
                    _userGroupRelationRepository.Add(new Domain.Permission.Relation.UserGroupRelation.UserGroupRelation()
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        IsDelete = 0,
                        UserGroupId = usergroup.Id,
                        UserId = model.Id
                    });
            }

            Repository.Add(model);
            return Repository.SaveChanges() > 0 ? Mapper.Map<UserInfoOutputDto>(model) : null;
        }

        public bool DeleteUserInfo(string id)
        {
            Repository.Remove(id, false);
            _userGroupRelationRepository.Remove(x => x.UserId.Equals(id));
            return Repository.SaveChanges() > 0;
        }

        /// <summary>
        ///     账号密码登录
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

        /// <summary>
        /// 根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByUserGroup(UserGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByUserGroup(input.id, input.usergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items?.AsQueryable().ProjectTo<UserInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        /// <summary>
        ///  根据权限组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByPowerGroup(PowerGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items?.AsQueryable().ProjectTo<UserInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        public Pages<List<UserInfoOutputDto>> GetUserByRoleGroup(RoleGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByRoleGroup(input.id, input.rolegroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items?.AsQueryable().ProjectTo<UserInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);

        }
    }
}