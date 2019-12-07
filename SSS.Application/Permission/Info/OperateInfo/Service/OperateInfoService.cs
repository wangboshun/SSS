using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.OperateInfo;
using SSS.Infrastructure.Repository.Permission.Info.PowerInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Info.OperateInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IOperateInfoService))]
    public class OperateInfoService :
        QueryService<Domain.Permission.Info.OperateInfo.OperateInfo, OperateInfoInputDto, OperateInfoOutputDto>,
        IOperateInfoService
    {
        private readonly IOperateInfoRepository _repository;
        private readonly IPowerInfoRepository _powerInfoRepository;
        private readonly IPowerGroupRepository _powerGroupRepository;
        private readonly IPowerGroupOperateRelationRepository _powerGroupOperateRelationRepository;

        public OperateInfoService(IMapper mapper,
            IOperateInfoRepository repository,
            IErrorHandler error,
            IValidator<OperateInfoInputDto> validator,
            IPowerInfoRepository powerInfoRepository,
            IPowerGroupRepository powerGroupRepository,
            IPowerGroupOperateRelationRepository powerGroupOperateRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _powerInfoRepository = powerInfoRepository;
            _powerGroupRepository = powerGroupRepository;
            _powerGroupOperateRelationRepository = powerGroupOperateRelationRepository;
        }

        public void AddOperateInfo(OperateInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var operate = Get(x => x.OperateName.Equals(input.operatename));
            if (operate != null)
            {
                Error.Execute("操作权限名已存在！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.OperateInfo.OperateInfo>(input);
            model.CreateTime = DateTime.Now;

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                var powergroup = _powerGroupRepository.Get(x => x.Id.Equals(input.powergroupid));
                if (powergroup != null)
                    _powerGroupOperateRelationRepository.Add(new PowerGroupOperateRelation()
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        OperateId = model.Id,
                        PowerGroupId = powergroup.Id,
                        IsDelete = 0
                    });
            }

            Repository.Add(model);
            Repository.SaveChanges();
        }

        public void DeleteOperateInfo(OperateInfoInputDto input)
        {
            Repository.Remove(input.id, false);
            _powerGroupOperateRelationRepository.Remove(x => x.OperateId.Equals(input.id));
            Repository.SaveChanges();
        }

        /// <summary>
        ///     获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        public List<OperateInfoTreeOutputDto> GetChildrenById(string operateid)
        {
            return _repository.GetChildrenById(operateid);
        }

        public Pages<List<OperateInfoOutputDto>> GetListOperateInfo(OperateInfoInputDto input)
        {
            return GetPage(input);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联操作
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupOperateRelationOutputDto>> GetOperateByPowerGroup(PowerGroupOperateRelationInputDto input)
        {
            return _powerGroupOperateRelationRepository.GetOperateByPowerGroup(input.powergroupid, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
        }
    }
}