using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.CQRS.UserInfo.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserInfo.Dto;
using SSS.Infrastructure.Repository.UserInfo;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Json;
using System;
using System.Collections.Generic;

namespace SSS.Application.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService : QueryService<SSS.Domain.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>, IUserInfoService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;

        private readonly IUserInfoRepository _repository;

        public UserInfoService(IMapper mapper, IUserInfoRepository repository, IEventBus bus) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
        }

        public void AddUserInfo(UserInfoInputDto input)
        {
            string appid = SSS.Infrastructure.Util.Config.Config.GetSectionValue("SenparcWeixinSetting:WxOpenAppId");
            string appsecret = SSS.Infrastructure.Util.Config.Config.GetSectionValue("SenparcWeixinSetting:WxOpenAppSecret");

            var result = Senparc.Weixin.WxOpen.AdvancedAPIs.Sns.SnsApi.JsCode2Json(appid, appsecret, input.code);

            var info = Senparc.Weixin.WxOpen.Helpers.EncryptHelper.DecodeEncryptedData(result.session_key, input.encryptedData, input.iv);

            input.id = Guid.NewGuid().ToString();
            input.openid = info.GetJsonValue("openId");
            input.name = info.GetJsonValue("nickName");

            var cmd = _mapper.Map<UserInfoAddCommand>(input);
            _bus.SendCommand(cmd);
        }

        public Pages<List<UserInfoOutputDto>> GetListUser(UserInfoInputDto input)
        {
            return GetList(input);
        }

        public UserInfoOutputDto GetUserInfo(UserInfoInputDto input)
        {
            return Get(input.id);
        }

        public UserInfoOutputDto GetUserInfoById(string userid)
        {
            return Get(userid);
        }
    }
}