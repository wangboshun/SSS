using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.RoleRoleGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.RoleRoleGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.RoleRoleGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleRoleGroupRelationService))]
    public class RoleRoleGroupRelationService : QueryService<SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation, RoleRoleGroupRelationInputDto, RoleRoleGroupRelationOutputDto>, IRoleRoleGroupRelationService
    {
        public RoleRoleGroupRelationService(IMapper mapper,
            IRoleRoleGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<RoleRoleGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddRoleRoleGroupRelation(RoleRoleGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.RoleRoleGroupRelation.RoleRoleGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RoleRoleGroupRelationOutputDto>> GetListRoleRoleGroupRelation(RoleRoleGroupRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteRoleRoleGroupRelation(RoleRoleGroupRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}