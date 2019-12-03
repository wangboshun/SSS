using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using SSS.Domain.Permission.Relation.PowerOperateRelation.Dto;
using SSS.Infrastructure.Repository.Permission.Relation.PowerOperateRelation;

namespace SSS.Application.Permission.Relation.PowerOperateRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerOperateRelationService))]
    public class PowerOperateRelationService :
        QueryService<Domain.Permission.Relation.PowerOperateRelation.PowerOperateRelation, PowerOperateRelationInputDto, PowerOperateRelationOutputDto>,
        IPowerOperateRelationService
    {
        public PowerOperateRelationService(IMapper mapper,
            IPowerOperateRelationRepository repository,
            IErrorHandler error,
            IValidator<PowerOperateRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddPowerOperateRelation(PowerOperateRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Relation.PowerOperateRelation.PowerOperateRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerOperateRelationOutputDto>> GetListPowerOperateRelation(PowerOperateRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerOperateRelation(PowerOperateRelationInputDto input)
        {
            Delete(input.id);
        }
    }
}