using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using SSS.Domain.Permission.Relation.RolePowerRelation.Dto;
using SSS.Infrastructure.Repository.Permission.Relation.RolePowerRelation;

namespace SSS.Application.Permission.Relation.RolePowerRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRolePowerRelationService))]
    public class RolePowerRelationService :
        QueryService<Domain.Permission.Relation.RolePowerRelation.RolePowerRelation, RolePowerRelationInputDto, RolePowerRelationOutputDto>,
        IRolePowerRelationService
    {
        public RolePowerRelationService(IMapper mapper,
            IRolePowerRelationRepository repository,
            IErrorHandler error,
            IValidator<RolePowerRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddRolePowerRelation(RolePowerRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Relation.RolePowerRelation.RolePowerRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RolePowerRelationOutputDto>> GetListRolePowerRelation(RolePowerRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeleteRolePowerRelation(RolePowerRelationInputDto input)
        {
            Delete(input.id);
        }
    }
}