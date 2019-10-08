using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Activity.Dto;
using SSS.Domain.CQRS.Activity.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Activity;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Activity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IActivityService))]
    public class ActivityService : QueryService<SSS.Domain.Activity.Activity, ActivityInputDto, ActivityOutputDto>, IActivityService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;
        private readonly IActivityRepository _repository;
        private readonly ILogger _logger;
        private readonly MemoryCacheEx _memorycache;

        public ActivityService(IMapper mapper, MemoryCacheEx memorycache, IActivityRepository repository, IEventBus bus, ILogger<ActivityService> logger) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
            _memorycache = memorycache;
            _logger = logger;
        }

        public void AddActivity(ActivityInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<ActivityAddCommand>(input);
            _bus.SendCommand(cmd);
        }

        public Pages<List<ActivityOutputDto>> GetListActivity(ActivityInputDto input)
        {
            return GetList(input);
        }

        public ActivityOutputDto GetById(ActivityInputDto input)
        {
            return _mapper.Map<ActivityOutputDto>(_repository.Get(x => x.Id.Equals(input.id) && x.IsDelete == 0));
        }
    }
}