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
using System;
using System.Collections.Generic;
using SSS.Domain.Seedwork.Notice;

namespace SSS.Application.UserInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserInfoService))]
    public class UserInfoService : QueryService<SSS.Domain.UserInfo.UserInfo, UserInfoInputDto, UserInfoOutputDto>,
        IUserInfoService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;
        private readonly ILogger _logger;
        private readonly IUserInfoRepository _repository;

        public UserInfoService(IMapper mapper, IUserInfoRepository repository, IEventBus bus, ILogger<UserInfoService> logger) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
            _logger = logger;
        }

        public void AddUserInfo(UserInfoInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<UserInfoAddCommand>(input);
            _bus.SendCommand(cmd);
        }

        public UserInfoOutputDto GetByUserName(UserInfoInputDto input)
        {
            var model = _repository.Get(x => x.UserName.Equals(input.username) && x.PassWord.Equals(input.password));
            if (model == null)
            {
                _bus.RaiseEvent(new ErrorNotice(input.GetType().Name, "’Àªß√‹¬Î¥ÌŒÛ£°"));
                return null;
            }

            return _mapper.Map<UserInfoOutputDto>(model);
        }

        public Pages<List<UserInfoOutputDto>> GetListUserInfo(UserInfoInputDto input)
        {
            return GetList(input);
        }
    }
}