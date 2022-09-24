package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.resource.ResourceEnum;
import com.zny.common.resource.ResourceModel;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.MenuMapper;
import com.zny.user.model.menu.MenuModel;
import com.zny.user.model.menu.MenuTreeModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class MenuApplication extends ServiceImpl<MenuMapper, MenuModel> {

    private final ResourceApplication resourceApplication;

    public MenuApplication(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 添加菜单
     *
     * @param menuName  菜单名
     * @param menuCode  菜单代码
     * @param parentId  父级id
     * @param menuIndex 菜单序号
     * @param menuUrl   菜单url
     * @param menuIcon  菜单图标
     * @param menuType  菜单类型：链接、按钮
     */
    public SaResult addMenu(
            String menuName, String menuCode, String parentId, Integer menuIndex, String menuUrl, String menuIcon,
            Integer menuType) {
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
        wrapper.eq("menu_name", menuName);
        MenuModel model = this.getOne(wrapper);
        if (model != null) {
            return SaResult.error("菜单名已存在！");
        }
        MenuModel menuModel = new MenuModel();
        menuModel.setId(UUID.randomUUID().toString());
        menuModel.setMenu_name(menuName);
        menuModel.setMenu_code(menuCode);
        menuModel.setParent_id(parentId);
        menuModel.setMenu_index(menuIndex);
        menuModel.setMenu_url(menuUrl);
        menuModel.setMenu_icon(menuIcon);
        menuModel.setMenu_type(menuType);
        menuModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(menuModel)) {
            return SaResult.ok("添加菜单成功！");
        }
        else {
            return SaResult.error("添加菜单失败！");
        }
    }

    /**
     * 查询菜单树
     *
     * @param menuId 菜单id
     */
    public List<MenuTreeModel> getMenuTree(String menuId) {
        List<MenuTreeModel> list = new ArrayList<>();

        //查找根目录
        if (menuId == null) {
            QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
            wrapper.isNull("parent_id");

            //查找没有父级id的菜单
            List<MenuModel> menuList = this.list(wrapper);
            for (MenuModel menu : menuList) {
                list.add(getChildren(menu.getId(), menu.getMenu_name(), 1));
            }
        }
        else {
            QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
            wrapper.eq(StringUtils.isNotBlank(menuId), "id", menuId);
            MenuModel menu = this.getOne(wrapper);
            list.add(getChildren(menu.getId(), menu.getMenu_name(), 1));
        }
        return list;
    }

    /**
     * 获取子级
     *
     * @param menuId 菜单id
     * @param level  树形等级
     */
    private MenuTreeModel getChildren(String menuId, String menuName, Integer level) {
        MenuTreeModel tree = new MenuTreeModel();
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
        wrapper.eq(StringUtils.isNotBlank(menuId), "parent_id", menuId);
        List<MenuModel> children = this.list(wrapper);
        tree.setId(menuId);
        tree.setLelvel(level);
        tree.setMenu_name(menuName);
        if (children.size() > 0) {
            for (MenuModel menu : children) {
                tree.setChildren(getChildren(menu.getId(), menu.getMenu_name(), level + 1));
            }
        }
        return tree;
    }

    /**
     * 查询菜单列表
     *
     * @param menuName  菜单名
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    public Map<String, Object> getMenuList(
            String menuId, String menuName, String menuCode, Integer pageIndex, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
        wrapper.eq(StringUtils.isNotBlank(menuId), "id", menuId);
        wrapper.eq(StringUtils.isNotBlank(menuName), "menu_name", menuName);
        wrapper.eq(StringUtils.isNotBlank(menuCode), "menu_code", menuCode);
        Page<MenuModel> page = new Page<>(pageIndex, pageSize);
        Page<MenuModel> result = this.page(page, wrapper);
        Map<String, Object> map = new HashMap<>(4);
        map.put("total", result.getTotal());
        map.put("rows", result.getRecords());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        return map;
    }

    /**
     * 删除菜单
     *
     * @param id 用户id
     */
    public SaResult deleteMenu(String id) {
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
        wrapper.eq("id", id);
        MenuModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("菜单不存在！");
        }
        if (removeById(id)) {
            return SaResult.ok("删除菜单成功！");
        }
        else {
            return SaResult.error("删除菜单失败！");
        }
    }

    /**
     * 更新菜单信息
     *
     * @param id        菜单id
     * @param menuName  菜单名
     * @param menuCode  菜单代码
     * @param parentId  父级id
     * @param menuIndex 菜单序号
     * @param menuUrl   菜单url
     * @param menuIcon  菜单图标
     * @param menuType  菜单类型：链接、按钮
     */
    public SaResult updateMenu(
            String id, String menuName, String menuCode, String parentId, Integer menuIndex, String menuUrl,
            String menuIcon, Integer menuType) {
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
        wrapper.eq("id", id);
        MenuModel menuModel = this.getOne(wrapper);

        if (menuModel == null) {
            return SaResult.error("菜单不存在！");
        }
        menuModel.setMenu_name(menuName);
        menuModel.setMenu_code(menuCode);
        menuModel.setParent_id(parentId);
        menuModel.setMenu_index(menuIndex);
        menuModel.setMenu_url(menuUrl);
        menuModel.setMenu_icon(menuIcon);
        menuModel.setMenu_type(menuType);
        if (updateById(menuModel)) {
            return SaResult.ok("更新菜单信息成功！");
        }
        else {
            return SaResult.error("删除菜单信息失败！");
        }
    }

    /**
     * 根据用户获取菜单
     *
     * @param userId 用户id
     */
    public List<MenuModel> getMenuByUser(String userId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(userId, ResourceEnum.USER.getIndex(), ResourceEnum.MENU.getIndex());
        List<MenuModel> menuList = new ArrayList<MenuModel>(getMenuByResourceModel(resourceList));

        //获取所有角色
        List<String> roleList = resourceApplication.getRoleByUser(userId);

        //遍历角色id，获取资源
        for (String roleId : roleList) {
            menuList.addAll(getMenuByRole(roleId));
        }

        return menuList;
    }


    /**
     * 根据角色获取菜单
     *
     * @param roleId 角色id
     */
    public List<MenuModel> getMenuByRole(String roleId) {
        List<ResourceModel> resourceList = resourceApplication.getResourceList(roleId, ResourceEnum.ROLE.getIndex(), ResourceEnum.MENU.getIndex());
        return new ArrayList<MenuModel>(getMenuByResourceModel(resourceList));
    }

    /**
     * 根据资源映射获取菜单
     *
     * @param list 资源列表
     */
    private List<MenuModel> getMenuByResourceModel(List<ResourceModel> list) {
        List<MenuModel> menuList = new ArrayList<MenuModel>();
        for (ResourceModel resourceModel : list) {
            MenuModel menuModel = this.getById(resourceModel.getSlave_id());
            menuList.add(menuModel);
        }
        return menuList;
    }

    /**
     * 绑定菜单到用户
     *
     * @param userId 用户id
     * @param menuId 菜单id
     */
    public SaResult bindMenuByUser(String userId, String[] menuId) {
        return resourceApplication.addResource(userId, ResourceEnum.USER.getIndex(), menuId, ResourceEnum.MENU.getIndex());
    }

    /**
     * 绑定菜单到角色
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    public SaResult bindMenuByRole(String roleId, String[] menuId) {
        return resourceApplication.addResource(roleId, ResourceEnum.ROLE.getIndex(), menuId, ResourceEnum.MENU.getIndex());
    }

    /**
     * 解绑菜单到用户
     *
     * @param userId 用户id
     * @param menuId  id
     */
    public SaResult unBindMenuByUser(String userId, String[] menuId) {
        return resourceApplication.deleteResource(null, userId, ResourceEnum.USER.getIndex(), menuId, ResourceEnum.MENU.getIndex());
    }

    /**
     * 解绑菜单到角色
     *
     * @param roleId 角色id
     * @param menuId  id
     */
    public SaResult unBindMenuByRole(String roleId, String[] menuId) {
        return resourceApplication.deleteResource(null, roleId, ResourceEnum.ROLE.getIndex(), menuId, ResourceEnum.MENU.getIndex());
    }
}
