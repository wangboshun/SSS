package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.user.application.MenuApplication;
import com.zny.user.model.menu.MenuModel;
import com.zny.user.model.menu.MenuTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author WBS
 * Date:2022/9/6
 * 菜单控制器
 */

@RestController
@RequestMapping("/user/menu")
@Tag(name = "menu", description = "菜单模块")
public class MenuController {

    private final MenuApplication menuApplication;

    public MenuController(MenuApplication menuApplication) {
        this.menuApplication = menuApplication;
    }

    /**
     * 获取菜单列表
     *
     * @param menuId   菜单id
     * @param menuName 菜单名
     * @param pageSize 分页大小
     */
    @GetMapping(value = "/list")
    public SaResult list(

            @RequestParam(required = false) String menuId, @RequestParam(required = false) String menuName,
            @RequestParam(required = false) String menuCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        PageResult result = menuApplication.getMenuPage(menuId, menuName, menuCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取菜单树
     *
     * @param menuId 菜单id
     */
    @GetMapping(value = "/tree")
    public SaResult tree(@RequestParam(required = false) String menuId) {
        List<MenuTreeModel> result = menuApplication.getMenuTree(menuId);
        return SaResult.data(result);
    }

    /**
     * 获取菜单信息
     *
     * @param id 菜单id
     */
    @GetMapping(value = "/{id}")
    public SaResult get(@PathVariable String id) {
        return SaResult.data(menuApplication.getMenuById(id));
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
    @PostMapping(value = "/add")
    public SaResult add(
            String menuName, String menuCode, @RequestParam(required = false) String parentId,
            @RequestParam(required = false) Integer menuIndex, @RequestParam(required = false) String menuUrl,
            @RequestParam(required = false) String menuIcon, @RequestParam(required = false) Integer menuType) {
        return menuApplication.addMenu(menuName, menuCode, parentId, menuIndex, menuUrl, menuIcon, menuType);
    }


    /**
     * 删除菜单
     *
     * @param id 菜单id
     */
    @DeleteMapping(value = "/{id}")
    public SaResult delete(@PathVariable String id) {
        return menuApplication.deleteMenu(id);
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
    @PatchMapping(value = "/{id}")
    public SaResult update(
            @PathVariable String id, @RequestParam(required = false) String menuName, @RequestParam(required = false) String menuCode, @RequestParam(required = false) String parentId,
            @RequestParam(required = false) Integer menuIndex, @RequestParam(required = false) String menuUrl,
            @RequestParam(required = false) String menuIcon, @RequestParam(required = false) Integer menuType) {
        return menuApplication.updateMenu(id, menuName, menuCode, parentId, menuIndex, menuUrl, menuIcon, menuType);
    }

    /**
     * 根据用户获取菜单
     *
     * @param userId 用户id
     */
    @GetMapping(value = "/by_user")
    public SaResult getMenuByUser(String userId) {
        List<MenuModel> list = menuApplication.getMenuByUser(userId);
        return SaResult.data(list);
    }

    /**
     * 根据角色获取菜单
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/by_role")
    public SaResult getMenuByRole(String roleId) {
        List<MenuModel> list = menuApplication.getMenuByRole(roleId);
        return SaResult.data(list);
    }


    /**
     * 绑定菜单到用户
     *
     * @param userIds 用户id
     * @param menuIds 菜单id
     */
    @PatchMapping(value = "/bind_by_user")
    public SaResult bindMenuByUser(String[] userIds, String[] menuIds) {
        return menuApplication.bindMenuByUser(userIds, menuIds);
    }

    /**
     * 绑定菜单到角色
     *
     * @param roleIds 角色id
     * @param menuIds 菜单id
     */
    @PatchMapping(value = "/bind_by_role")
    public SaResult bindMenuByRole(String[] roleIds, String[] menuIds) {
        return menuApplication.bindMenuByRole(roleIds, menuIds);
    }

    /**
     * 解绑菜单到用户
     *
     * @param userIds 用户id
     * @param menuIds id
     */
    @PatchMapping(value = "/unbind_by_user")
    public SaResult unBindMenuByUser(String[] userIds, String[] menuIds) {
        return menuApplication.unBindMenuByUser(userIds, menuIds);
    }

    /**
     * 解绑菜单到角色
     *
     * @param roleIds 角色id
     * @param menuIds id
     */
    @PatchMapping(value = "/unbind_by_role")
    public SaResult unBindMenuByRole(String[] roleIds, String[] menuIds) {
        return menuApplication.unBindMenuByRole(roleIds, menuIds);
    }
}
