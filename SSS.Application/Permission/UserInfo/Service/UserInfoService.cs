using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.UserInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.UserInfo;
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
        private readonly IUserInfoRepository _repository;

        public UserInfoService(IMapper mapper,
            IUserInfoRepository repository,
            IErrorHandler error,
            IValidator<UserInfoInputDto> validator,
            MemoryCacheEx memorycache) : base(mapper, repository, error, validator)
        {
            _memorycache = memorycache;
            _repository = repository;
        }

        /// <summary>
        /// GetChildren
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public List<UserInfoTreeOutputDto> GetChildren(UserInfoInputDto input)
        {
            return _repository.GetChildren(input);
        }

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
            Repository.Add(model, true);
        }

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