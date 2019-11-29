using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.OperateInfo;
using SSS.Domain.Permission.OperateInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.OperateInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.OperateInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IOperateInfoService))]
    public class OperateInfoService : QueryService<SSS.Domain.Permission.OperateInfo.OperateInfo, OperateInfoInputDto, OperateInfoOutputDto>, IOperateInfoService
    {
        private readonly IOperateInfoRepository _repository;

        public OperateInfoService(IMapper mapper,
            IOperateInfoRepository repository,
            IErrorHandler error,
            IValidator<OperateInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
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
            var model = Mapper.Map<SSS.Domain.Permission.OperateInfo.OperateInfo>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        /// <summary>
        /// 获取操作下的所有下级
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
    }
}