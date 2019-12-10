using AutoMapper;
using AutoMapper.QueryableExtensions;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.MenuInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation;
using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Info.MenuInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoService))]
    public class MenuInfoService :
        QueryService<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoInputDto, MenuInfoOutputDto>, IMenuInfoService
    {
        private readonly IMenuInfoRepository _menuInfoRepository;
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
            _menuInfoRepository = repository;
            _powerGroupRepository = powerGroupRepository;
            _powerGroupMenuRelationRepository = powerGroupMenuRelationRepository;
        }

        public bool AddMenuInfo(MenuInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            var menu = Get(x => x.MenuName.Equals(input.menuname));
            if (menu != null)
            {
                Error.Execute("菜单名已存在！");
                return false;
            }

            input.id = Guid.NewGuid().ToString();
            var model = Mapper.Map<Domain.Permission.Info.MenuInfo.MenuInfo>(input);
            model.CreateTime = DateTime.Now;

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                var powergroup = _powerGroupRepository.Get(input.powergroupid);
                if (powergroup != null)
                    _powerGroupMenuRelationRepository.Add(new Domain.Permission.Relation.PowerGroupMenuRelation.PowerGroupMenuRelation
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        MenuId = model.Id,
                        PowerGroupId = powergroup.Id,
                        IsDelete = 0
                    });
            }

            Repository.Add(model);
            return Repository.SaveChanges()>0;
        }

        public bool DeleteMenuInfo(string id)
        {
            Repository.Remove(id, false);
            _powerGroupMenuRelationRepository.Remove(x => x.MenuId.Equals(id));
            return Repository.SaveChanges()>0;
        }

        /// <summary>
        ///     获取菜单下的所有下级
        /// </summary>
        /// <param name="menuid"></param>
        /// <returns></returns>
        public List<MenuInfoTreeOutputDto> GetChildren(string menuid)
        {
            return _menuInfoRepository.GetChildren(menuid);
        }

        /// <summary>
        /// 根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<MenuInfoOutputDto>> GetMenuByPowerGroup(PowerGroupInputDto input)
        {
            var data = _menuInfoRepository.GetMenuByPowerGroup(input.id, input.powergroupname, input.parentid, input.pageindex, input.pagesize);
            return new Pages<List<MenuInfoOutputDto>>(data.items.AsQueryable().ProjectTo<MenuInfoOutputDto>(Mapper.ConfigurationProvider).ToList(), data.count);
        }

        public Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}