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
        ///     ��ȡ�û�������Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public object GetUserPermission(string userid)
        {
            var user = _userinfoRepository.Get(userid);
            if (user == null)
                return new { error = "�û�������" };

            var usergroup = _userGroupRepository.GetUserGroupByUser(user.Id, user.UserName).items.MapperToOutPut<UserGroupOutputDto>();

            var rolegroup = _roleGroupRepository.GetRoleGroupByUser(user.Id, user.UserName).items.MapperToOutPut<RoleGroupOutputDto>();

            var powergroup = _powerGroupRepository.GetPowerGroupByUser(user.Id, user.UserName).items.MapperToOutPut<PowerGroupOutputDto>();

            var role = _roleInfoRepository.GetRoleByUser(user.Id, user.UserName).items.MapperToOutPut<RoleInfoOutputDto>();

            var power = _powerInfoRepository.GetPowerByUser(user.Id, user.UserName).items.MapperToOutPut<PowerInfoOutputDto>();

            var menu = _menuInfoRepository.GetMenuByUser(user.Id, user.UserName).items.MapperToOutPut<MenuInfoOutputDto>();

            var operate = _operateInfoRepository.GetOperateByUser(user.Id, user.UserName).items.MapperToOutPut<OperateInfoOutputDto>();

            return new
            {
                usergroup,
                rolegroup,
                powergroup,
                role,
                power,
                menu,
                operate
            };
        }

        /// <summary>
        ///     ɾ���û�����Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public bool DeleteUserPermission(string userid)
        {
            return true;
        }

        /// <summary>
        ///     ��ȡ�û��µ������¼�
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public List<UserInfoTreeOutputDto> GetChildrenById(string userid)
        {
            return _userinfoRepository.GetChildrenById(userid);
        }

        /// <summary>
        ///     ����û�
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
                Error.Execute("�û��Ѵ��ڣ�");
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
        ///     �˺������¼
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public UserInfoOutputDto GetByUserName(UserInfoInputDto input)
        {
            var result = Get(x => x.UserName.Equals(input.username) && x.PassWord.Equals(input.password));
            if (result == null)
            {
                Error.Execute("�˻��������");
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
        ///     �����û���Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByUserGroup(UserGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByUserGroup(input.id, input.usergroupname, input.parentid,
                input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items.MapperToOutPut<UserInfoOutputDto>()?.ToList(), data.count);
        }

        /// <summary>
        ///     ����Ȩ����Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByPowerGroup(PowerGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByPowerGroup(input.id, input.powergroupname, input.parentid,
                input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items.MapperToOutPut<UserInfoOutputDto>()?.ToList(), data.count);
        }

        /// <summary>
        ///     ���ݽ�ɫ��Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserInfoOutputDto>> GetUserByRoleGroup(RoleGroupInputDto input)
        {
            var data = _userinfoRepository.GetUserByRoleGroup(input.id, input.rolegroupname, input.parentid,
                input.pageindex, input.pagesize);
            return new Pages<List<UserInfoOutputDto>>(data.items.MapperToOutPut<UserInfoOutputDto>()?.ToList(), data.count);
        }
    }
}