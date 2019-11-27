using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
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
        public OperateInfoService(IMapper mapper,
            IOperateInfoRepository repository,
            IErrorHandler error,
            IValidator<OperateInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {

        }

        public void AddOperateInfo(OperateInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.OperateInfo.OperateInfo>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<OperateInfoOutputDto>> GetListOperateInfo(OperateInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}