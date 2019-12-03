using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.RoleUserGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.RoleUserGroupRelation;
using SSS.Infrastructure.Util.Attribute;
using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.RoleUserGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleUserGroupRelationService))]
    public class RoleUserGroupRelationService : QueryService<SSS.Domain.Permission.Relation.RoleUserGroupRelation.RoleUserGroupRelation, RoleUserGroupRelationInputDto, RoleUserGroupRelationOutputDto>, IRoleUserGroupRelationService
    {
        private readonly IRoleUserGroupRelationRepository _roleUserGroupRelationRepository;

        public RoleUserGroupRelationService(IMapper mapper,
            IRoleUserGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<RoleUserGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _roleUserGroupRelationRepository = repository;
        }

        public void AddRoleUserGroupRelation(RoleUserGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.RoleUserGroupRelation.RoleUserGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RoleUserGroupRelationOutputDto>> GetListRoleUserGroupRelation(RoleUserGroupRelationInputDto input)
        {
            return GetPage(input);
        } 
        public void DeleteRoleUserGroupRelation(RoleUserGroupRelationInputDto input)
        {
            Delete(input.id);
        }
    }
}