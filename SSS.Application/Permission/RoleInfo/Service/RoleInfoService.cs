using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.RoleInfo;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.RoleInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleInfoService))]
    public class RoleInfoService : QueryService<SSS.Domain.Permission.RoleInfo.RoleInfo, RoleInfoInputDto, RoleInfoOutputDto>, IRoleInfoService
    {
        private readonly IRoleInfoRepository _repository;

        public RoleInfoService(IMapper mapper,
            IRoleInfoRepository repository,
            IErrorHandler error,
            IValidator<RoleInfoInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
        }

        public void AddRoleInfo(RoleInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.RoleInfo.RoleInfo>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public List<RoleInfoTreeOutputDto> GetChildren(RoleInfoInputDto input)
        {
            return _repository.GetChildren(input);
        }

        public Pages<List<RoleInfoOutputDto>> GetListRoleInfo(RoleInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}