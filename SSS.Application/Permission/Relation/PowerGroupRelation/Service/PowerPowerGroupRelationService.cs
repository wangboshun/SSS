using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerGroupRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupRelationService))]
    public class PowerGroupRelationService : QueryService<SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation, PowerGroupRelationInputDto, PowerGroupRelationOutputDto>, IPowerGroupRelationService
    {
        public PowerGroupRelationService(IMapper mapper,
            IPowerGroupRelationRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddPowerGroupRelation(PowerGroupRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.Relation.PowerGroupRelation.PowerGroupRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerGroupRelationOutputDto>> GetListPowerGroupRelation(PowerGroupRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerGroupRelation(PowerGroupRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}