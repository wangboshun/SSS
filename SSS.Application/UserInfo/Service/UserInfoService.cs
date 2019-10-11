using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Application.Seedwork.Service;
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
        private readonly IMapper _mapper;
        private readonly ILogger _logger;
        private readonly IUserInfoRepository _repository;
        private readonly MemoryCacheEx _memorycache;

        public UserInfoService(IMapper mapper, MemoryCacheEx memorycache, IUserInfoRepository repository, ILogger<UserInfoService> logger) : base(mapper, repository)
        {
            _mapper = mapper;
            _repository = repository;
            _memorycache = memorycache;
            _logger = logger;
        }

        public void AddUserInfo(UserInfoInputDto input)
        {
            var result = _repository.Get(x => x.UserName.Equals(input.username));
            if (result != null)
            {
                //_bus.RaiseEvent(new ErrorNotice(input.GetType().Name, "�û��Ѵ��ڣ�"));
                return;
            }

            input.id = Guid.NewGuid().ToString();
        }

        public UserInfoOutputDto GetByUserName(UserInfoInputDto input)
        {
            var result = _repository.Get(x => x.UserName.Equals(input.username) && x.PassWord.Equals(input.password));
            if (result == null)
            {
                //_bus.RaiseEvent(new ErrorNotice(input.GetType().Name, "�˻��������"));
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