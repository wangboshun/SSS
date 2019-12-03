using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Relation.PowerPowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupService))]
    public class PowerGroupService :
        QueryService<Domain.Permission.Group.PowerGroup.PowerGroup, PowerGroupInputDto, PowerGroupOutputDto>,
        IPowerGroupService
    {
        private readonly IPowerPowerGroupRelationRepository _powerPowerGroupRelationRepository;

        public PowerGroupService(IMapper mapper,
            IPowerGroupRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupInputDto> validator,
            IPowerPowerGroupRelationRepository powerPowerGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _powerPowerGroupRelationRepository = powerPowerGroupRelationRepository;
        }

        public void AddPowerGroup(PowerGroupInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Group.PowerGroup.PowerGroup>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerGroupOutputDto>> GetListPowerGroup(PowerGroupInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerGroup(PowerGroupInputDto input)
        {
            Delete(input.id);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerPowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerPowerGroupRelationInputDto input)
        {
            return _powerPowerGroupRelationRepository.GetPowerGroupByPower(input);
        }
    }
}