using System;
using System.Collections.Generic;
using AutoMapper;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Application.Permission.Relation.PowerMenuRelation.Service;
using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerMenuRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.PowerMenuRelation;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Application.Permission.Relation.PowerMenu.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerMenuRelationService))]
    public class PowerMenuRelationService :
        QueryService<Domain.Permission.Relation.PowerMenuRelation.PowerMenuRelation, PowerMenuRelationInputDto, PowerMenuRelationOutputDto>,
        IPowerMenuRelationService
    {
        public PowerMenuRelationService(IMapper mapper,
            IPowerMenuRelationRepository repository,
            IErrorHandler error,
            IValidator<PowerMenuRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddPowerMenuRelation(PowerMenuRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Relation.PowerMenuRelation.PowerMenuRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerMenuRelationOutputDto>> GetListPowerMenuRelation(PowerMenuRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerMenuRelation(PowerMenuRelationInputDto input)
        {
            Delete(input.id);
        }
    }
}