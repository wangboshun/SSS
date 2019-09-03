using AutoMapper;
using AutoMapper.QueryableExtensions;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using SSS.Application.Seedwork.Service;
using SSS.Application.Trade.Service;
using SSS.Domain.CQRS.UserApi.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Notice;
using SSS.Domain.UserApi.Dto;
using SSS.Domain.UserApi.Response;
using SSS.Infrastructure.Repository.UserApi;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;

namespace SSS.Application.UserApi.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserApiService))]
    public class UserApiService : QueryService<SSS.Domain.UserApi.UserApi, UserApiInputDto, UserApiOutputDto>, IUserApiService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;
        private readonly ILogger _logger;

        private readonly IUserApiRepository _repository;
        public UserApiService(ILogger<UserApiService> logger, IMapper mapper, IEventBus bus, IUserApiRepository repository) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
            _logger = logger;
        }
        public void AddUserApi(UserApiInputDto input)
        {
            var list = GetMarginAccounts(input.ApiKey, input.Secret, input.PassPhrase);
            if (list != null && list.Count > 0)
            {
                input.id = Guid.NewGuid().ToString();
                input.Status = 1;
                var cmd = _mapper.Map<UserApiAddCommand>(input);
                _bus.SendCommand(cmd);
            }
            else
                _bus.RaiseEvent(new ErrorNotice("UserApi", "≈‰÷√Api ß∞‹,«Î÷ÿ–¬ ‰»Î!"));
        }

        public void UpdateUserApi(UserApiInputDto input)
        {
            var list = GetMarginAccounts(input.ApiKey, input.Secret, input.PassPhrase);
            if (list != null && list.Count > 0)
            {
                var cmd = _mapper.Map<UserApiUpdateCommand>(input);
                _bus.SendCommand(cmd);
            }
            else
                _bus.RaiseEvent(new ErrorNotice("UserApi", "≈‰÷√Api ß∞‹,«Î÷ÿ–¬ ‰»Î!"));
        }

        public Pages<List<UserApiOutputDto>> GetListUserApi(UserApiInputDto input)
        {
            List<UserApiOutputDto> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                var temp = _repository.GetAll();
                list = _repository.GetAll().ProjectTo<UserApiOutputDto>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _repository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<UserApiOutputDto>(_mapper.ConfigurationProvider).ToList();

            return new Pages<List<UserApiOutputDto>>(list, count);
        }

        public UserApiOutputDto GetByUserId(UserApiInputDto input)
        {
            return Get(x => x.UserId.Equals(input.UserId));
        }

        private List<MarginAccounts> GetMarginAccounts(string ApiKey, string Secret, string PassPhrase)
        {
            var url = "https://www.okex.me/api/margin/v3/accounts";
            using (var client = new HttpClient(new HttpInterceptor(ApiKey, Secret, PassPhrase, null)))
            {
                var res = client.GetAsync($"{url}").Result;

                if (res.StatusCode != HttpStatusCode.OK)
                    return null;
                var result = res.Content.ReadAsStringAsync().Result;
                _logger.LogInformation($"GetMarginAccounts {result}");
                return JsonConvert.DeserializeObject<List<MarginAccounts>>(result);
            }
        }
    }
}