using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.CQRS.UserConfig.Command.Commands;
using SSS.Infrastructure.Util.Attribute;
using SSS.Domain.UserConfig.Dto;
using SSS.Infrastructure.Repository.UserConfig;
using System;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using AutoMapper.QueryableExtensions;
using System.Linq;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.UserConfig.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserConfigService))]
    public class UserConfigService : QueryService<SSS.Domain.UserConfig.UserConfig, UserConfigInputDto, UserConfigOutputDto>, IUserConfigService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;

        private readonly IUserConfigRepository _repository;
        public UserConfigService(IMapper mapper, IEventBus bus, IUserConfigRepository repository) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
        }
        public void AddUserConfig(UserConfigInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<UserConfigAddCommand>(input);
            _bus.SendCommand(cmd);
        }

        public UserConfigOutputDto GetConfig(UserConfigInputDto input)
        {
            return Get(x => x.Id.Equals(input.id));
        }

        public Pages<List<UserConfigOutputDto>> GetListUserConfig(UserConfigInputDto input)
        {
            List<UserConfigOutputDto> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                var data = _repository.GetAll(x => x.UserId.Equals(input.UserId));
                list = data.ProjectTo<UserConfigOutputDto>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _repository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<UserConfigOutputDto>(_mapper.ConfigurationProvider).ToList();

            return new Pages<List<UserConfigOutputDto>>(list, count);
        }
    }
}