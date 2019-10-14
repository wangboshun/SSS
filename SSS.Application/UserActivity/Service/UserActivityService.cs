using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
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
        private readonly ILogger _logger;
        private readonly MemoryCacheEx _memorycache;
        private readonly IUserActivityRepository _repository;
        private IValidator<UserActivityInputDto> _validator;
        private readonly IErrorHandler _error;

        public UserActivityService(IValidator<UserActivityInputDto> validator, IMapper mapper,
            MemoryCacheEx memorycache, IUserActivityRepository repository,
            ILogger<UserActivityService> logger, IErrorHandler error) : base(mapper, repository)
        {
            _mapper = mapper;
            _repository = repository;
            _memorycache = memorycache;
            _logger = logger;
            _validator = validator;
            _error = error;
        }

        public void AddUserActivity(UserActivityInputDto input)
        {
            var result = _validator.Validate(input);
            if (!result.IsValid)
            {
                _error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = _mapper.Map<SSS.Domain.UserActivity.UserActivity>(input);
            _repository.Add(model);
            _repository.SaveChanges();
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