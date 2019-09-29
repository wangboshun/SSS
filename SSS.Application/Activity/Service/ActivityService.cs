using AutoMapper;
using AutoMapper.QueryableExtensions;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Activity.Dto;
using SSS.Domain.CQRS.Activity.Command.Commands;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Activity;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Activity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IActivityService))]
    public class ActivityService : IActivityService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;

        private readonly IActivityRepository _repository;
        public ActivityService(IMapper mapper, IEventBus bus, IActivityRepository repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
        }
        public void AddActivity(ActivityInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<ActivityAddCommand>(input);
            _bus.SendCommand(cmd);
        }

        public Pages<List<ActivityOutputDto>> GetListActivity(ActivityInputDto input)
        {
            List<ActivityOutputDto> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                var temp = _repository.GetAll();
                list = _repository.GetAll().ProjectTo<ActivityOutputDto>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _repository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<ActivityOutputDto>(_mapper.ConfigurationProvider).ToList();

            return new Pages<List<ActivityOutputDto>>(list, count);
        }

        public ActivityOutputDto GetById(ActivityInputDto input)
        {
            return _mapper.Map<ActivityOutputDto>(_repository.Get(x => x.Id.Equals(input.id) && x.IsDelete == 0));
        }
    }
}