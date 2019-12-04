using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.UserGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.UserGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.UserGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserGroupRelationService))]
    public class UserGroupRelationService : QueryService<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation, UserGroupRelationInputDto, UserGroupRelationOutputDto>, IUserGroupRelationService
    {
        private readonly IUserGroupRelationRepository _userGroupRelationRepository;

        public UserGroupRelationService(IMapper mapper,
            IUserGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<UserGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _userGroupRelationRepository = repository;
        }

        public void AddUserGroupRelation(UserGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.UserGroupRelation.UserGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<UserGroupRelationOutputDto>> GetListUserGroupRelation(UserGroupRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteUserGroupRelation(UserGroupRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}