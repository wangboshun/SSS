using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Relation.PowerGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Group.PowerGroup.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerGroupService))]
    public class PowerGroupService :
        QueryService<Domain.Permission.Group.PowerGroup.PowerGroup, PowerGroupInputDto, PowerGroupOutputDto>,
        IPowerGroupService
    {
        private readonly IPowerGroupRelationRepository _powerGroupRelationRepository;

        public PowerGroupService(IMapper mapper,
            IPowerGroupRepository repository,
            IErrorHandler error,
            IValidator<PowerGroupInputDto> validator,
            IPowerGroupRelationRepository powerGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _powerGroupRelationRepository = powerGroupRelationRepository;
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
            Repository.Remove(input.id);
        }

        /// <summary>
        /// 根据权限Id或名称，遍历关联权限组
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupRelationOutputDto>> GetPowerGroupByPower(PowerGroupRelationInputDto input)
        {
            return _powerGroupRelationRepository.GetPowerGroupByPower(input);
        }
    }
}