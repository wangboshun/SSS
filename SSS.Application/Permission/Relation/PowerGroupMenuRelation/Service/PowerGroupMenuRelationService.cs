using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Permission.Relation.PowerGroupMenuRelation.Service;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerGroupMenu.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupMenuRelationService))]
    public class PowerGroupMenuRelationService :
        QueryService<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation, PowerGroupMenuRelationInputDto, PowerGroupMenuRelationOutputDto>,
        IPowerGroupMenuRelationService
    {
        public PowerGroupMenuRelationService(IMapper mapper,
            IPowerGroupMenuRelationRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupMenuRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddPowerGroupMenuRelation(PowerGroupMenuRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerGroupMenuRelationOutputDto>> GetListPowerGroupMenuRelation(PowerGroupMenuRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerGroupMenuRelation(PowerGroupMenuRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}