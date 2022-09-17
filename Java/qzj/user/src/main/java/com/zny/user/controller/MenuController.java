package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.google.common.collect.Table;
import com.zny.user.application.MenuApplication;
import com.zny.user.application.ResourceApplication;
import com.zny.user.model.menu.MenuModel;
import com.zny.user.model.menu.MenuTreeModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/user/menu")
@Tag(name = "menu", description = "菜单模块")
public class MenuController {

    private final MenuApplication menuApplication;

    private final ResourceApplication resourceApplication;

    public MenuController(MenuApplication menuApplication, ResourceApplication resourceApplication) {
        this.menuApplication = menuApplication;
        this.resourceApplication = resourceApplication;
    }

    /**
     * 获取菜单列表
     *
     * @param menuId   菜单id
     * @param menuName 菜单名
     * @param pageSize 分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(

            @RequestParam(required = false) String menuId, @RequestParam(required = false) String menuName,
            @RequestParam(required = false) String menuCode, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = menuApplication.getMenuList(menuId, menuName, menuCode, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取菜单树
     *
     * @param menuId 菜单id
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public SaResult tree(@RequestParam(required = false) String menuId) {
        List<MenuTreeModel> result = menuApplication.getMenuTree(menuId);
        return SaResult.data(result);
    }

    /**
     * 获取菜单信息
     *
     * @param id 菜单id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        MenuModel model = menuApplication.getById(id);
        return SaResult.data(model);
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
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
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
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
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(
            @PathVariable String id, String menuName, String menuCode, @RequestParam(required = false) String parentId,
            @RequestParam(required = false) Integer menuIndex, @RequestParam(required = false) String menuUrl,
            @RequestParam(required = false) String menuIcon, @RequestParam(required = false) Integer menuType) {
        return menuApplication.updateMenu(id, menuName, menuCode, parentId, menuIndex, menuUrl, menuIcon, menuType);
    }
}
