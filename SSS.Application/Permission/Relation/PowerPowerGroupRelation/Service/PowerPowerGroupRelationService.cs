using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.PowerPowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerPowerGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerPowerGroupRelationService))]
    public class PowerPowerGroupRelationService : QueryService<SSS.Domain.Permission.Relation.PowerPowerGroupRelation.PowerPowerGroupRelation, PowerPowerGroupRelationInputDto, PowerPowerGroupRelationOutputDto>, IPowerPowerGroupRelationService
    {
        public PowerPowerGroupRelationService(IMapper mapper,
            IPowerPowerGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<PowerPowerGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddPowerPowerGroupRelation(PowerPowerGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.PowerPowerGroupRelation.PowerPowerGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerPowerGroupRelationOutputDto>> GetListPowerPowerGroupRelation(PowerPowerGroupRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerPowerGroupRelation(PowerPowerGroupRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}