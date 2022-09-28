package com.zny.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.common.model.PageResult;
import com.zny.common.resource.ResourceApplication;
import com.zny.common.resource.ResourceModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author WBS
 * Date:2022/9/6
 */

@RestController
@RequestMapping("/system/resource")
@Tag(name = "resource", description = "资源模块")
public class ResourceController {
    private final ResourceApplication resourceApplication;

    public ResourceController(ResourceApplication resourceApplication) {
        this.resourceApplication = resourceApplication;
    }

    /**
     * 获取资源列表
     *
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     * @param pageIndex 页码
     * @param pageSize  分页大小
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(
            @RequestParam(required = false) String id, @RequestParam(required = false) String mainId,
            @RequestParam(required = false) Integer mainType, @RequestParam(required = false) String slaveId,
            @RequestParam(required = false) Integer slaveType, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        PageResult result = resourceApplication.getResourcePage(id, mainId, mainType, slaveId, slaveType, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取资源信息
     *
     * @param id 资源id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        ResourceModel model = resourceApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 添加资源
     *
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(
            String mainId, int mainType, @RequestParam(required = false) String[] slaveId, int slaveType) {
        return resourceApplication.addResource(mainId, mainType, slaveId, slaveType);
    }

    /**
     * 删除资源
     *
     * @param ids id或id组
     */
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String[] ids) {
        boolean b = resourceApplication.removeBatchByIds(Arrays.asList(ids));
        if (b) {
            return SaResult.ok("资源删除成功！");
        }
        else {
            return SaResult.error("资源删除失败！");
        }
    }

    /**
     * 更新资源信息
     *
     * @param id        id
     * @param mainId    主id
     * @param mainType  主类型
     * @param slaveId   副id
     * @param slaveType 副类型
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(
            @PathVariable String id, String mainId, Integer mainType, String slaveId, Integer slaveType) {
        return resourceApplication.updateResource(id, mainId, mainType, slaveId, slaveType);
    }
}
