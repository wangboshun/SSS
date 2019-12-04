using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.UserInfo.Dto;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.UserGroup;
using SSS.Infrastructure.Repository.Permission.Info.UserInfo;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Info.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService :
        QueryService<Domain.Permission.Info.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>,
        IUserInfoService
    {
        private readonly MemoryCacheEx _memorycache;
        private readonly IUserInfoRepository _userinfoRepository;
        private readonly IUserGroupRelationRepository _userGroupRelationRepository;
        private readonly IUserGroupRepository _userGroupRepository;

        public UserInfoService(IMapper mapper,
            IUserInfoRepository repository,
            IErrorHandler error,
            IValidator<UserInfoInputDto> validator,
            MemoryCacheEx memorycache,
            IUserGroupRelationRepository userGroupRelationRepository,
            IUserGroupRepository userGroupRepository
        ) : base(mapper, repository, error, validator)
        {
            _memorycache = memorycache;
            _userinfoRepository = repository;
            _userGroupRelationRepository = userGroupRelationRepository;
            _userGroupRepository = userGroupRepository;
        }

        /// <summary>
        ///     ��ȡ�û�������Ȩ��
        /// </summary>
        /// <param name="userid"></param>
        /// <returns></returns>
        public object GetUserPermission(string userid)
        {
            var menu = "";
            var operate = "";

            return new { menu, operate };
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
                Error.Execute("�û��Ѵ��ڣ�");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.UserInfo.UserInfo>(input);
            model.CreateTime = DateTime.Now;

            var group = _userGroupRepository.Get(x => x.Id.Equals(input.usergroupid));

            //����û������
            _userGroupRelationRepository.Add(new Domain.Permission.Relation.UserGroupRelation.UserGroupRelation()
            {
                CreateTime = DateTime.Now,
                Id = Guid.NewGuid().ToString(),
                IsDelete = 0,
                UserGroupId = group != null ? group.Id : "0",
                UserId = model.Id
            });

            Repository.Add(model);
            Repository.SaveChanges();
        }

        public void DeleteUserInfo(UserInfoInputDto input)
        {
            Repository.Remove(input.id, false);
            _userGroupRelationRepository.Remove(x => x.UserId.Equals(input.id));
            Repository.SaveChanges();
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
        /// �����û���Id�����ƣ����������û�
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<UserGroupRelationOutputDto>> GetUserListByGroup(UserGroupRelationInputDto input)
        {
            return _userGroupRelationRepository.GetUserListByGroup(input);
        }
    }
}