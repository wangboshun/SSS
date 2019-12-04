using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.RoleGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.RoleGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.RoleGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleGroupRelationService))]
    public class RoleGroupRelationService : QueryService<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation, RoleGroupRelationInputDto, RoleGroupRelationOutputDto>, IRoleGroupRelationService
    {
        public RoleGroupRelationService(IMapper mapper,
            IRoleGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<RoleGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddRoleGroupRelation(RoleGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.RoleGroupRelation.RoleGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RoleGroupRelationOutputDto>> GetListRoleGroupRelation(RoleGroupRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteRoleGroupRelation(RoleGroupRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}