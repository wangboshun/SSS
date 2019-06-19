using AutoMapper;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.CQRS.Articel.Command.Commands;
using SSS.Domain.Seedwork.Attribute;
using SSS.Domain.Articel.Dto;
using SSS.Infrastructure.Repository.Articel;
using System;
using SSS.Domain.Seedwork.EventBus;
using SSS.Domain.Seedwork.Model;
using System.Collections.Generic;
using AutoMapper.QueryableExtensions;
using System.Linq;

namespace SSS.Application.Articel
{
    [DIService(ServiceLifetime.Scoped, typeof(IArticelService))]
    public class ArticelService : IArticelService
    {
        private readonly IMapper _mapper;
        private readonly IEventBus _bus;

        private readonly IArticelRepository _repository;
        public ArticelService(IMapper mapper, IEventBus bus, IArticelRepository repository)
        {
            _mapper = mapper;
            _bus = bus;
            _repository = repository;
        }
        public void AddArticel(ArticelInputDto input)
        {
            input.id = Guid.NewGuid().ToString();
            var cmd = _mapper.Map<ArticelAddCommand>(input);
            _bus.SendCommand(cmd);
        }

		public Pages<List<ArticelOutputDto>> GetListArticel(ArticelInputDto input) 
		{
           List<ArticelOutputDto> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                var temp = _repository.GetAll();
                list = _repository.GetAll().ProjectTo<ArticelOutputDto>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _repository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<ArticelOutputDto>(_mapper.ConfigurationProvider).ToList();

            return new Pages<List<ArticelOutputDto>>(list, count);}
      } 
}