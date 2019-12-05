using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.MenuInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;

namespace SSS.Application.Permission.Info.MenuInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoService))]
    public class MenuInfoService :
        QueryService<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoInputDto, MenuInfoOutputDto>, IMenuInfoService
    {
        private readonly IMenuInfoRepository _repository;
        private readonly IPowerGroupRepository _powerGroupRepository;
        private readonly IPowerGroupMenuRelationRepository _powerGroupMenuRelationRepository;

        public MenuInfoService(IMapper mapper,
            IMenuInfoRepository repository,
            IErrorHandler error,
            IValidator<MenuInfoInputDto> validator,
            IPowerGroupRepository powerGroupRepository,
            IPowerGroupMenuRelationRepository powerGroupMenuRelationRepository) :
            base(mapper, repository, error, validator)
        {
            _repository = repository;
            _powerGroupRepository = powerGroupRepository;
            _powerGroupMenuRelationRepository = powerGroupMenuRelationRepository;
        }

        public void AddMenuInfo(MenuInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return;
            }

            var menu = Get(x => x.MenuName.Equals(input.menuname));
            if (menu != null)
            {
                Error.Execute("菜单名已存在！");
                return;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.MenuInfo.MenuInfo>(input);
            model.CreateTime = DateTime.Now;

            //PowerGroupMenu
            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                //查出权限组
                var powergroup = _powerGroupRepository.Get(input.powergroupid);

                //添加权限组于菜单关联关系
                _powerGroupMenuRelationRepository.Add(new Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation
                {
                    CreateTime = DateTime.Now,
                    Id = Guid.NewGuid().ToString(),
                    MenuId = model.Id,
                    PowerGroupId = powergroup != null ? powergroup.Id : "0",
                    IsDelete = 0
                });
            }

            Repository.Add(model);
            Repository.SaveChanges();
        }

        public void DeleteMenuInfo(MenuInfoInputDto input)
        {
            Repository.Remove(input.id, false);
            _powerGroupMenuRelationRepository.Remove(x => x.MenuId.Equals(input.id));
            Repository.SaveChanges();
        }

        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<MenuInfoTreeOutputDto> GetChildren(string menuid)
        {
            return _repository.GetChildren(menuid);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<PowerGroupMenuRelationOutputDto>> GetMenuListByPowerGroup(PowerGroupMenuRelationInputDto input)
        {
            return _powerGroupMenuRelationRepository.GetMenuListByPowerGroup(input);
        }

        public Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}