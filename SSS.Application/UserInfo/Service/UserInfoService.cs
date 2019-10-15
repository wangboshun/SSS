using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Repository.UserInfo;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService : QueryService<SSS.Domain.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>,
        IUserInfoService
    {
        private readonly MemoryCacheEx _memorycache;

        public UserInfoService(IMapper mapper,
            IUserInfoRepository repository,
            IErrorHandler error,
            IValidator<UserInfoInputDto> validator,
            MemoryCacheEx memorycache) : base(mapper, repository, error, validator)
        {
            _memorycache = memorycache;
        }

        public void AddUserInfo(UserInfoInputDto input)
        {
            var result = _validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                _error.Execute(result);
                return;
            }

            var user = Get(x => x.UserName.Equals(input.username));
            if (user != null)
            {
                _error.Execute("用户已存在！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = _mapper.Map<SSS.Domain.UserInfo.UserInfo>(input);
            _repository.Add(model, true);
        }

        public UserInfoOutputDto GetByUserName(UserInfoInputDto input)
        {
            var result = Get(x => x.UserName.Equals(input.username) && x.PassWord.Equals(input.password));
            if (result == null)
            {
                _error.Execute("账户密码错误！");
                return null;
            }

            var userinfo = _mapper.Map<UserInfoOutputDto>(result);
            _memorycache.Set("AuthUserInfo_" + userinfo.id, userinfo, 60 * 24);
            return userinfo;
        }

        public Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input)
        {
            return GetList(input);
        }
    }
}