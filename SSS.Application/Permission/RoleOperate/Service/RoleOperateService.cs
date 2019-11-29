using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleOperate.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.OperateInfo;
using SSS.Infrastructure.Repository.Permission.RoleInfo;
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
        private readonly IRoleInfoRepository _roleInfoRepository;
        private readonly IOperateInfoRepository _operateInfoRepository;

        public RoleOperateService(IMapper mapper,
            IRoleOperateRepository repository,
            IErrorHandler error,
            IValidator<RoleOperateInputDto> validator,
            IOperateInfoRepository operateInfoRepository,
            IRoleInfoRepository roleInfoRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _roleInfoRepository = roleInfoRepository;
            _operateInfoRepository = operateInfoRepository;
        }

        public void AddRoleOperate(RoleOperateInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var operate = _operateInfoRepository.Get(input.operateid);
            if (operate == null)
            {
                Error.Execute("操作权限错误！");
                return;
            }

            var role = _roleInfoRepository.Get(input.roleid);
            if (role == null)
            {
                Error.Execute("用户错误！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.RoleOperate.RoleOperate>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        /// <summary>
        /// 删除角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        public bool DeleteRoleOperateByRole(string roleid)
        {
            return _repository.DeleteRoleOperateByRole(roleid);
        }

        public Pages<List<RoleOperateOutputDto>> GetListRoleOperate(RoleOperateInputDto input)
        {
            return GetPage(input);
        }

        /// <summary>
        /// 获取角色下的所有操作
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleOperateOutputDto> GetOperateByRole(string roleid)
        {
            return _repository.GetRoleOperateByRole(roleid);
        }
    }
}