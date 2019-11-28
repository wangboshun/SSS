using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.RoleOperate;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.RoleOperate.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleOperateService))]
    public class RoleOperateService : QueryService<SSS.Domain.Permission.RoleOperate.RoleOperate, RoleOperateInputDto, RoleOperateOutputDto>, IRoleOperateService
    {
        private readonly IRoleOperateRepository _repository;
        public RoleOperateService(IMapper mapper,
            IRoleOperateRepository repository,
            IErrorHandler error,
            IValidator<RoleOperateInputDto> validator) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
        }

        public void AddRoleOperate(RoleOperateInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.RoleOperate.RoleOperate>(input);
            Repository.Add(model);
            Repository.SaveChanges();
        }

        public Pages<List<RoleOperateOutputDto>> GetListRoleOperate(RoleOperateInputDto input)
        {
            return GetPage(input);
        }

        public List<RoleOperateOutputDto> GetOperateByRole(string roleid)
        {
            return _repository.GetOperateByRole(roleid);
        }
    }
}