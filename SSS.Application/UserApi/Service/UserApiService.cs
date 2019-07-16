using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.CQRS.UserApi.Command.Commands;
using SSS.Infrastructure.Util.Attribute;
using SSS.Domain.UserApi.Dto;
using SSS.Infrastructure.Repository.UserApi;
using System;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using AutoMapper.QueryableExtensions;
using System.Linq;
using SSS.Application.Seedwork.Service;

namespace SSS.Application.UserApi.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserApiService))]
    public class UserApiService : QueryService<SSS.Domain.UserApi.UserApi, UserApiInputDto, UserApiOutputDto>, IUserApiService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;

        private readonly IUserApiRepository _repository;
        public UserApiService(IMapper mapper, IEventBus bus, IUserApiRepository repository) : base(mapper, repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
        }
        public void AddUserApi(UserApiInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<UserApiAddCommand>(input);
            _bus.SendCommand(cmd);
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

    }
}