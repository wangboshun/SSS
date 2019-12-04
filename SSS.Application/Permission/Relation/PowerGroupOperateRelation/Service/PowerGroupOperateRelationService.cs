using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Relation.PowerGroupOperateRelation.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupOperateRelationService))]
    public class PowerGroupOperateRelationService :
        QueryService<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation, PowerGroupOperateRelationInputDto, PowerGroupOperateRelationOutputDto>,
        IPowerGroupOperateRelationService
    {
        public PowerGroupOperateRelationService(IMapper mapper,
            IPowerGroupOperateRelationRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupOperateRelationInputDto> validator) :
            base(mapper, repository, error, validator)
        {
        }

        public void AddPowerGroupOperateRelation(PowerGroupOperateRelationInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Relation.PowerGroupOperateRelation.PowerGroupOperateRelation>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerGroupOperateRelationOutputDto>> GetListPowerGroupOperateRelation(PowerGroupOperateRelationInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerGroupOperateRelation(PowerGroupOperateRelationInputDto input)
        {
            Repository.Remove(input.id);
        }
    }
}