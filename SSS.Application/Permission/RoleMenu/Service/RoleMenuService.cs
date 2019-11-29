using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.RoleMenu.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.MenuInfo;
using SSS.Infrastructure.Repository.Permission.RoleInfo;
using SSS.Infrastructure.Repository.Permission.RoleMenu;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.RoleMenu.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IRoleMenuService))]
    public class RoleMenuService : QueryService<SSS.Domain.Permission.RoleMenu.RoleMenu, RoleMenuInputDto, RoleMenuOutputDto>, IRoleMenuService
    {
        private readonly IRoleMenuRepository _repository;
        private readonly IRoleInfoRepository _roleInfoRepository;
        private readonly IMenuInfoRepository _menuInfoRepository;

        public RoleMenuService(IMapper mapper,
            IRoleMenuRepository repository,
            IErrorHandler error,
            IValidator<RoleMenuInputDto> validator,
            IRoleInfoRepository roleInfoRepository,
            IMenuInfoRepository menuInfoRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _roleInfoRepository = roleInfoRepository;
            _menuInfoRepository = menuInfoRepository;
        }

        public void AddRoleMenu(RoleMenuInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var menu = _menuInfoRepository.Get(input.menuid);
            if (menu == null)
            {
                Error.Execute("�˵�����");
                return;
            }

            var role = _roleInfoRepository.Get(input.roleid);
            if (role == null)
            {
                Error.Execute("�û�����");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<SSS.Domain.Permission.RoleMenu.RoleMenu>(input);
            model.CreateTime = DateTime.Now;
            Repository.Add(model);
            Repository.SaveChanges();
        }

        /// <summary>
        /// ɾ����ɫ�µ����в˵�
        /// </summary>
        /// <param name="roleid"></param>
        public bool DeleteRoleMenuByRole(string roleid)
        {
            return _repository.DeleteRoleMenuByRole(roleid);
        }

        public Pages<List<RoleMenuOutputDto>> GetListRoleMenu(RoleMenuInputDto input)
        {
            return GetPage(input);
        }

        /// <summary>
        /// ��ȡ��ɫ�����в˵�
        /// </summary>
        /// <param name="roleid"></param>
        /// <returns></returns>
        public List<RoleMenuOutputDto> GetMenuByRole(string roleid)
        {
            return _repository.GetRoleMenuByRole(roleid);
        }
    }
}