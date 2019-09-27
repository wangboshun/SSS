using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.CQRS.UserActivity.Command.Commands;
using SSS.Infrastructure.Util.Attribute;
using SSS.Domain.UserActivity.Dto;
using SSS.Infrastructure.Repository.UserActivity;
using System;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using AutoMapper.QueryableExtensions;
using System.Linq;

namespace SSS.Application.UserActivity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserActivityService))]
    public class UserActivityService : IUserActivityService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;

        private readonly IUserActivityRepository _repository;
        public UserActivityService(IMapper mapper, IEventBus bus, IUserActivityRepository repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
        }
        public void AddUserActivity(UserActivityInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<UserActivityAddCommand>(input);
            var result = _bus.SendCommand(cmd);
        }

        public List<int> GetGroupNumberByName(UserActivityInputDto input)
        {
            List<int> GroupNumber = new List<int>();
            var list = _repository.GetAll(x => x.WechatName.Equals(input.wechatname) && x.ActivityId.Equals(input.activityid)).OrderBy(x => x.GroupNumber);
            foreach (var item in list)
            {
                GroupNumber.Add(item.GroupNumber);
            }

            return GroupNumber;
        }
        public Pages<List<UserActivityOutputDto>> GetListUserActivity(UserActivityInputDto input)
        {
            List<UserActivityOutputDto> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                var temp = _repository.GetAll();
                list = _repository.GetAll().ProjectTo<UserActivityOutputDto>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _repository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<UserActivityOutputDto>(_mapper.ConfigurationProvider).ToList();

            return new Pages<List<UserActivityOutputDto>>(list, count);
        }
    }
}