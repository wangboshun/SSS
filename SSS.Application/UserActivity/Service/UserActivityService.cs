using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Application.Seedwork.Service;
using SSS.Domain.CQRS.UserActivity.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserActivity.Dto;
using SSS.Infrastructure.Repository.UserActivity;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.UserActivity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserActivityService))]
    public class UserActivityService : QueryService<SSS.Domain.UserActivity.UserActivity, UserActivityInputDto, UserActivityOutputDto>, IUserActivityService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;
        private readonly ILogger _logger;
        private readonly MemoryCacheEx _memorycache;
        private readonly IUserActivityRepository _repository;

        public UserActivityService(IMapper mapper, MemoryCacheEx memorycache, IUserActivityRepository repository, IEventBus bus, ILogger<UserActivityService> logger) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
            _memorycache = memorycache;
            _logger = logger;
        }

        public void AddUserActivity(UserActivityInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<UserActivityAddCommand>(input);
            var result = _bus.SendCommand(cmd);
        }

        public List<int> GetGroupNumber(UserActivityInputDto input)
        {
            List<int> GroupNumber = new List<int>();
            var list = _repository.GetAll(x => x.UserId.Equals(input.userid) && x.ActivityId.Equals(input.activityid)).OrderBy(x => x.GroupNumber);
            foreach (var item in list)
            {
                GroupNumber.Add(item.GroupNumber);
            }

            return GroupNumber;
        }
        public Pages<List<UserActivityOutputDto>> GetListUserActivity(UserActivityInputDto input)
        {
            return GetList(input);
        }
    }
}