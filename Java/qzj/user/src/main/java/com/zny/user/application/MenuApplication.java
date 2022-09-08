package com.zny.user.application;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zny.common.utils.DateUtils;
import com.zny.user.mapper.MenuMapper;
import com.zny.user.model.MenuModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author WBS
 * Date:2022/9/2
 */

@Service
public class MenuApplication extends ServiceImpl<MenuMapper, MenuModel> {

    /**
     * 添加菜单
     *
     * @param menuName 菜单名
     */
    public SaResult addMenu(String menuName, String menuCode) {
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
        menuModel.setCreate_time(DateUtils.dateToStr(LocalDateTime.now()));
        if (save(menuModel)) {
            return SaResult.ok("添加菜单成功！");
        }
        else {
            return SaResult.error("添加菜单失败！");
        }
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
        Map<String, Object> map = new HashMap<>();
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
     * @param id       菜单id
     * @param menuName 菜单名
     */
    public SaResult updateMenu(String id, String menuName, String menuCode) {
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<MenuModel>();
        wrapper.eq("id", id);
        MenuModel model = this.getOne(wrapper);

        if (model == null) {
            return SaResult.error("菜单不存在！");
        }
        model.setMenu_name(menuName);
        model.setMenu_code(menuCode);
        if (updateById(model)) {
            return SaResult.ok("更新菜单信息成功！");
        }
        else {
            return SaResult.error("删除菜单信息失败！");
        }
    }
}
