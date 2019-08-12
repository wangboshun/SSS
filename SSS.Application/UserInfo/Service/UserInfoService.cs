using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
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
using System.Net;

namespace SSS.Application.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService : QueryService<SSS.Domain.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>, IUserInfoService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;
        private readonly ILogger _logger;

        private readonly IUserInfoRepository _repository;

        public UserInfoService(ILogger<UserInfoService> logger, IMapper mapper, IUserInfoRepository repository, IEventBus bus) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _logger = logger;
            _repository = repository;
        }

        public void AddUserInfo(UserInfoInputDto input)
        {
            string appid = SSS.Infrastructure.Util.Config.Config.GetSectionValue("SenparcWeixinSetting:WxOpenAppId");
            string appsecret = SSS.Infrastructure.Util.Config.Config.GetSectionValue("SenparcWeixinSetting:WxOpenAppSecret");
            string info = "";
            input.id = Guid.NewGuid().ToString();

            //string url = string.Format("https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code", appid, appsecret, input.code);
            //info = new WebClient().DownloadString(url);

            //input.openid = info.GetJsonValue("openid");
            //input.name = input.openid;

            var result = Senparc.Weixin.WxOpen.AdvancedAPIs.Sns.SnsApi.JsCode2Json(appid, appsecret, input.code);
            info = Senparc.Weixin.WxOpen.Helpers.EncryptHelper.DecodeEncryptedData(result.session_key, input.encryptedData, input.iv);
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
            return Get(x => x.Id.Equals(input.id) && x.IsDelete != 1);
        }

        public UserInfoOutputDto GetUserInfoByOpenId(string openid)
        {
            return Get(x => x.Openid.Equals(openid) && x.IsDelete != 1);
        }
    }
}