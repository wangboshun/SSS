using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Group.RoleGroup.Dto;
using SSS.Domain.Permission.Group.UserGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Info.RoleInfo.Dto;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Permission.Relation.UserGroupRelation;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Group.RoleGroup;
using SSS.Infrastructure.Repository.Permission.Group.UserGroup;
using SSS.Infrastructure.Repository.Permission.Info.MenuInfo;
using SSS.Infrastructure.Repository.Permission.Info.OperateInfo;
using SSS.Infrastructure.Repository.Permission.Info.PowerInfo;
using SSS.Infrastructure.Repository.Permission.Info.RoleInfo;
using SSS.Infrastructure.Repository.Permission.Info.UserInfo;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

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
        private readonly IMenuInfoRepository _menuInfoRepository;
        private readonly IRoleInfoRepository _roleInfoRepository;
        private readonly IUserInfoRepository _userinfoRepository;
        private readonly IPowerInfoRepository _powerInfoRepository;
        private readonly IRoleGroupRepository _roleGroupRepository;
        private readonly IUserGroupRepository _userGroupRepository;
        private readonly IPowerGroupRepository _powerGroupRepository;
        private readonly IOperateInfoRepository _operateInfoRepository;
        private readonly IUserGroupRelationRepository _userGroupRelationRepository;

        public UserInfoService(IMapper mapper,
            IUserInfoRepository repository,
            IErrorHandler error,
            IValidator<UserInfoInputDto> validator,
            MemoryCacheEx memorycache,
            IMenuInfoRepository menuInfoRepository,
            IRoleInfoRepository roleInfoRepository,
            IPowerInfoRepository powerInfoRepository,
            IRoleGroupRepository roleGroupRepository,
            IUserGroupRepository userGroupRepository,
            IPowerGroupRepository powerGroupRepository,
            IOperateInfoRepository operateInfoRepository,
            IUserGroupRelationRepository userGroupRelationRepository
        ) : base(mapper, repository, error, validator)
        {
            _memorycache = memorycache;
            _userinfoRepository = repository;
            _menuInfoRepository = menuInfoRepository;
            _roleInfoRepository = roleInfoRepository;
            _powerInfoRepository = powerInfoRepository;
            _userGroupRepository = userGroupRepository;
            _roleGroupRepository = roleGroupRepository;
            _powerGroupRepository = powerGroupRepository;
            _operateInfoRepository = operateInfoRepository;
            _userGroupRelationRepository = userGroupRelationRepository;
        }

        /// <summary>
        ///     获取用户下所有权限
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public object GetUserPermission(string userid)
        {
            List<UserGroupOutputDto> usergroup_list;
            List<RoleGroupOutputDto> rolegroup_list = new List<RoleGroupOutputDto>();
            List<PowerGroupOutputDto> powergroup_list = new List<PowerGroupOutputDto>();

            List<RoleInfoOutputDto> role_list = new List<RoleInfoOutputDto>();
            List<PowerInfoOutputDto> power_list = new List<PowerInfoOutputDto>();
            List<MenuInfoOutputDto> menu_list = new List<MenuInfoOutputDto>();
            List<OperateInfoOutputDto> operate_list = new List<OperateInfoOutputDto>();

            var user = _userinfoRepository.Get(userid);
            if (user == null)
                return new { error = "用户不存在" };

            usergroup_list = _userGroupRepository.GetUserGroupByUser(user.Id, user.UserName).items.MapperToOutPut<UserGroupOutputDto>().ToList();

            foreach (var usergroup in usergroup_list)
            {
                var ug_data = _roleGroupRepository.GetRoleGroupByUserGroup(usergroup?.id, usergroup?.usergroupname).items.MapperToOutPut<RoleGroupOutputDto>();
                if (ug_data != null)
                    rolegroup_list.AddRange(ug_data);

                var pg_data = _powerGroupRepository
                    .GetPowerGroupByUserGroup(usergroup?.id, usergroup?.usergroupname).items.MapperToOutPut<PowerGroupOutputDto>();
                if (pg_data != null)
                    powergroup_list.AddRange(pg_data);
            }

            foreach (var rolegroup in rolegroup_list)
            {
                var r_data = _roleInfoRepository.GetRoleByRoleGroup(rolegroup?.id, rolegroup?.rolegroupname).items.MapperToOutPut<RoleInfoOutputDto>();
                if (r_data != null)
                    role_list.AddRange(r_data.ToList());
            }

            foreach (var powergroup in powergroup_list)
            {
                var p_data = _powerInfoRepository.GetPowerByPowerGroup(powergroup?.id, powergroup?.powergroupname).items.MapperToOutPut<PowerInfoOutputDto>();
                if (p_data != null)
                    power_list.AddRange(p_data.ToList());

                var m_data = _menuInfoRepository.GetMenuByPowerGroup(powergroup?.id, powergroup?.powergroupname).items.MapperToOutPut<MenuInfoOutputDto>();
                if (m_data != null)
                    menu_list.AddRange(m_data.ToList());

                var o_data = _operateInfoRepository
                    .GetOperateByPowerGroup(powergroup?.id, powergroup?.powergroupname).items.MapperToOutPut<OperateInfoOutputDto>();
                if (o_data != null)
                    operate_list.AddRange(o_data);
            }

            return new { usergroup_list, role_list, power_list, menu_list, operate_list };
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
                    _userGroupRelationRepository.Add(new UserGroupRelation
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
            Repository.Remove(id);
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
        ///     根据用户组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByUserGroup(UserGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByUserGroup(input.id, input.usergroupname, input.parentid,
                input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items.MapperToOutPut<UserInfoOutputDto>().ToList(), data.count);
        }

        /// <summary>
        ///     根据权限组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByPowerGroup(PowerGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByPowerGroup(input.id, input.powergroupname, input.parentid,
                input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items.MapperToOutPut<UserInfoOutputDto>().ToList(), data.count);
        }

        /// <summary>
        ///     根据角色组Id或名称，遍历关联用户
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByRoleGroup(RoleGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByRoleGroup(input.id, input.rolegroupname, input.parentid,
                input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items.MapperToOutPut<UserInfoOutputDto>().ToList(), data.count);
        }
    }
}