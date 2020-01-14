using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.OperateInfo;
using SSS.Domain.Permission.Info.OperateInfo.Dto;
using SSS.Domain.Permission.Relation.PowerGroupOperateRelation;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.OperateInfo;
using SSS.Infrastructure.Repository.Permission.Info.PowerInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupOperateRelation;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Info.OperateInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IOperateInfoService))]
    public class OperateInfoService :
        QueryService<Domain.Permission.Info.OperateInfo.OperateInfo, OperateInfoInputDto, OperateInfoOutputDto>,
        IOperateInfoService
    {
        private readonly IOperateInfoRepository _operateInfoRepository;
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
            _operateInfoRepository = repository;
            _powerInfoRepository = powerInfoRepository;
            _powerGroupRepository = powerGroupRepository;
            _powerGroupOperateRelationRepository = powerGroupOperateRelationRepository;
        }

        public OperateInfoOutputDto AddOperateInfo(OperateInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            var operate = Get(x => x.OperateName.Equals(input.operatename));
            if (operate != null)
            {
                Error.Execute("操作权限名已存在！");
                return null;
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
            return Repository.SaveChanges() > 0 ? Mapper.Map<OperateInfoOutputDto>(model) : null;
        }

        public bool DeleteOperateInfo(string id)
        {
            Repository.Remove(id, false);
            _powerGroupOperateRelationRepository.Remove(x => x.OperateId.Equals(id));
            return Repository.SaveChanges() > 0;
        }

        /// <summary>
        ///     获取操作下的所有下级
        /// </summary>
        /// <param name="operateid"></param>
        /// <returns></returns>
        public List<OperateInfoTreeOutputDto> GetChildrenById(string operateid)
        {
            return _operateInfoRepository.GetChildrenById(operateid);
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
        public Pages<List<OperateInfoOutputDto>> GetOperateByPowerGroup(PowerGroupInputDto input)
        {
            var data = _operateInfoRepository.GetOperateByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<OperateInfoOutputDto>>(data.items.MapperToOutPut<OperateInfoOutputDto>()?.ToList(), data.count);
        }
    }
}