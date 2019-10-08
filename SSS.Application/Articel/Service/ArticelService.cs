using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Articel.Dto;
using SSS.Domain.CQRS.Articel.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Articel;
using SSS.Infrastructure.Seedwork.Cache.MemoryCache;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Articel.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IArticelService))]
    public class ArticelService : QueryService<SSS.Domain.Articel.Articel, ArticelInputDto, ArticelOutputDto>, IArticelService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;
        private readonly IArticelRepository _repository;
        private readonly ILogger _logger;
        private readonly MemoryCacheEx _memorycache;

        public ArticelService(IMapper mapper, MemoryCacheEx memorycache, IArticelRepository repository, IEventBus bus, ILogger<ArticelService> logger) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
            _memorycache = memorycache;
            _logger = logger;
        }

        public void AddArticel(ArticelInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<ArticelAddCommand>(input);
            _bus.SendCommand(cmd);
        }

        public Pages<List<ArticelOutputDto>> GetListArticel(ArticelInputDto input)
        {
            return GetList(input);
        }
    }
}