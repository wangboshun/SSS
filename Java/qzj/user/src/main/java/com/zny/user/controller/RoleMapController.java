package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.user.application.user.RoleMapApplication;
import com.zny.user.model.RoleMapModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/user/role_map")
@Tag(name = "role_map", description = "角色映射模块")
public class RoleMapController {

    @Autowired
    private RoleMapApplication roleMapMapApplication;

    /**
     * 获取映射列表
     *
     * @param id        映射id
     * @param mapName   映射名
     * @param roleId    角色id
     * @param mapId     映射代码
     * @param mapType   类型
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(@RequestParam(required = false) String id, @RequestParam(required = false) String roleId, @RequestParam(required = false) String mapName, @RequestParam(required = false) String mapId, @RequestParam(required = false) Integer mapType, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = roleMapMapApplication.getRoleMapList(id, roleId, mapId, mapName, mapType, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取映射信息
     *
     * @param id 映射id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        RoleMapModel model = roleMapMapApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 添加映射
     *
     * @param mapId   映射代码
     * @param roleId  角色id
     * @param mapType 类型
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String roleId, String mapId, Integer mapType) {
        return roleMapMapApplication.addRoleMap(roleId, mapId, mapType);
    }


    /**
     * 删除映射
     *
     * @param ids 映射id
     */
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String[] ids) {
        boolean b = roleMapMapApplication.removeBatchByIds(Arrays.asList(ids));
        if (b) {
            return SaResult.ok("日志删除成功！");
        } else {
            return SaResult.error("日志删除失败！");
        }
    }

    /**
     * 根据角色删除映射
     * @param roleId 角色id
     */
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String roleId) {
        return roleMapMapApplication.deleteByRoleId(roleId);
    }

    /**
     * 更新映射信息
     *
     * @param id      映射id
     * @param mapId   映射代码
     * @param roleId  角色id
     * @param mapType 类型
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(@PathVariable String id, String roleId,String mapId, Integer mapType) {
        return roleMapMapApplication.updateRoleMap(id, roleId, mapId, mapType);
    }
}
