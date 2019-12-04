using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.UserUserGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.UserUserGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.UserUserGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IUserUserGroupRelationService))]
    public class UserUserGroupRelationService : QueryService<SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation, UserUserGroupRelationInputDto, UserUserGroupRelationOutputDto>, IUserUserGroupRelationService
    {
        private readonly IUserUserGroupRelationRepository _userUserGroupRelationRepository;

        public UserUserGroupRelationService(IMapper mapper,
            IUserUserGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<UserUserGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _userUserGroupRelationRepository = repository;
        }

        public void AddUserUserGroupRelation(UserUserGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.UserUserGroupRelation.UserUserGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<UserUserGroupRelationOutputDto>> GetListUserUserGroupRelation(UserUserGroupRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteUserUserGroupRelation(UserUserGroupRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}