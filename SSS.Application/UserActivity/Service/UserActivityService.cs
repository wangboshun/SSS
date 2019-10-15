using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.UserActivity.Dto;
using SSS.Infrastructure.Repository.UserActivity;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.UserActivity.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserActivityService))]
    public class UserActivityService : QueryService<SSS.Domain.UserActivity.UserActivity, UserActivityInputDto, UserActivityOutputDto>, IUserActivityService
    {
        public UserActivityService(IMapper mapper,
            IUserActivityRepository repository,
            IErrorHandler error, IValidator<UserActivityInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddUserActivity(UserActivityInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.UserActivity.UserActivity>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public List<int> GetGroupNumber(UserActivityInputDto input)
        {
            List<int> GroupNumber = new List<int>();
            var list = Repository.GetAll(x => x.UserId.Equals(input.userid) && x.ActivityId.Equals(input.activityid)).OrderBy(x => x.GroupNumber);
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