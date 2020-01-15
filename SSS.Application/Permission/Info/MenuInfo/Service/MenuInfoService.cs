using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Application.Seedwork.Service;
using SSS.Domain.Permission.Group.PowerGroup.Dto;
using SSS.Domain.Permission.Info.MenuInfo.Dto;
using SSS.Domain.Permission.Relation.PowerGroupMenuRelation;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Infrastructure.Repository.Permission.Group.PowerGroup;
using SSS.Infrastructure.Repository.Permission.Info.MenuInfo;
using SSS.Infrastructure.Repository.Permission.Relation.PowerGroupMenuRelation;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;

namespace SSS.Application.Permission.Info.MenuInfo.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IMenuInfoService))]
    public class MenuInfoService :QueryService<Domain.Permission.Info.MenuInfo.MenuInfo, MenuInfoInputDto, MenuInfoOutputDto>, IMenuInfoService
    {
        private readonly IMenuInfoRepository _menuInfoRepository;
        private readonly IPowerGroupMenuRelationRepository _powerGroupMenuRelationRepository;
        private readonly IPowerGroupRepository _powerGroupRepository;

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

        public MenuInfoOutputDto AddMenuInfo(MenuInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Insert");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return null;
            }

            var menu = _menuInfoRepository.Get(x => x.MenuName.Equals(input.menuname));
            if (menu != null)
            {
                Error.Execute("菜单名已存在！");
                return null;
            }

            if (!GetParent(input.parentid))
                return null;

            input.id = Guid.NewGuid().ToString();
            menu = Mapper.Map<Domain.Permission.Info.MenuInfo.MenuInfo>(input);
            menu.CreateTime = DateTime.Now;

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                var powergroup = _powerGroupRepository.Get(input.powergroupid);
                if (powergroup != null)
                    _powerGroupMenuRelationRepository.Add(new PowerGroupMenuRelation
                    {
                        CreateTime = DateTime.Now,
                        Id = Guid.NewGuid().ToString(),
                        MenuId = menu.Id,
                        PowerGroupId = powergroup.Id,
                        IsDelete = 0
                    });
            }

            Repository.Add(menu);
            return Repository.SaveChanges() > 0 ? Mapper.Map<MenuInfoOutputDto>(menu) : null;
        }

        public bool UpdateMenuInfo(MenuInfoInputDto input)
        {
            var result = Validator.Validate(input, ruleSet: "Update");
            if (!result.IsValid)
            {
                Error.Execute(result);
                return false;
            }

            var menu = _menuInfoRepository.Get(input.id);
            if (menu == null)
            {
                Error.Execute("菜单不存在！");
                return false;
            }

            if (!AddParentId(menu.ParentId, input.parentid, out var parentid))
                return false;

            menu.MenuName = input.menuname;
            menu.MenuUrl = input.menuurl;
            menu.ParentId = parentid;

            var pgmr = _powerGroupMenuRelationRepository.Get(x => x.MenuId.Equals(menu.Id));

            if (!string.IsNullOrWhiteSpace(input.powergroupid))
            {
                var powergroup = _powerGroupRepository.Get(input.powergroupid);
                if (powergroup != null)
                {
                    //如果没有映射关系，增加
                    if (pgmr == null)
                    {
                        _powerGroupMenuRelationRepository.Add(new PowerGroupMenuRelation
                        {
                            CreateTime = DateTime.Now,
                            Id = Guid.NewGuid().ToString(),
                            MenuId = menu.Id,
                            PowerGroupId = powergroup.Id,
                            IsDelete = 0
                        });
                    }
                    else
                    {
                        //如果不等于现有映射PowerGroupId，更新
                        if (!pgmr.PowerGroupId.Equals(input.powergroupid))
                        {
                            pgmr.IsDelete = 0;
                            pgmr.UpdateTime = DateTime.Now;
                            pgmr.PowerGroupId = powergroup.Id;
                            _powerGroupMenuRelationRepository.Update(pgmr);
                        }
                    }
                }
                else
                {
                    Error.Execute("权限组不存在！");
                    return false;
                }
            }
            else
            {
                //如果映射不为空，更新映射
                if (pgmr != null)
                {
                    pgmr.IsDelete = 1;
                    pgmr.UpdateTime = DateTime.Now;
                    _powerGroupMenuRelationRepository.Update(pgmr);
                }
            }

            menu.UpdateTime = DateTime.Now;
            Repository.Update(menu);
            return Repository.SaveChanges() > 0;
        }

        public bool DeleteMenuInfo(string id)
        {
            Repository.Remove(id);
            _powerGroupMenuRelationRepository.Remove(x => x.MenuId.Equals(id));
            return Repository.SaveChanges() > 0;
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
        ///     根据权限组Id或名称，遍历关联菜单
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public Pages<List<MenuInfoOutputDto>> GetMenuByPowerGroup(PowerGroupInputDto input)
        {
            var data = _menuInfoRepository.GetMenuByPowerGroup(input.id, input.powergroupname, input.parentid,input.pageindex, input.pagesize);
            return new Pages<List<MenuInfoOutputDto>>(data.items.MapperToOutPut<MenuInfoOutputDto>()?.ToList(),data.count);
        }

        public Pages<List<MenuInfoOutputDto>> GetListMenuInfo(MenuInfoInputDto input)
        {
            return GetPage(input);
        }
    }
}