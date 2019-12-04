using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.PowerInfo.Dto;
using SSS.Domain.Permission.Relation.PowerPowerGroupRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Info.PowerInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerPowerGroupRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Info.PowerInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IPowerInfoService))]
    public class PowerInfoService :
        QueryService<Domain.Permission.Info.PowerInfo.PowerInfo, PowerInfoInputDto, PowerInfoOutputDto>,
        IPowerInfoService
    {
        private readonly IPowerInfoRepository _repository;

        private readonly IPowerPowerGroupRelationRepository _powerPowerGroupRelationRepository;

        public PowerInfoService(IMapper mapper,
            IPowerInfoRepository repository,
            IErrorHandler error,
            IValidator<PowerInfoInputDto> validator,
            IPowerPowerGroupRelationRepository powerPowerGroupRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _powerPowerGroupRelationRepository = powerPowerGroupRelationRepository;
        }

        public void AddPowerInfo(PowerInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.PowerInfo.PowerInfo>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<PowerInfoOutputDto>> GetListPowerInfo(PowerInfoInputDto input)
        {
            return GetPage(input);
        }

        public void DeletePowerInfo(PowerInfoInputDto input)
        {
            Repository.Remove(input.id);
        }

        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<PowerInfoTreeOutputDto> GetChildren(string menuid)
        {
            return _repository.GetChildren(menuid);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联权限
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerPowerGroupRelationOutputDto>> GetPowerListByGroup(PowerPowerGroupRelationInputDto input)
        {
            return _powerPowerGroupRelationRepository.GetPowerListByGroup(input);
        }
    }
}