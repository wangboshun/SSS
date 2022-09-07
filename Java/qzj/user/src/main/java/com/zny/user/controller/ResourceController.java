package com.zny.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.user.application.ResourceApplication;
import com.zny.user.model.ResourceModel;
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
@RequestMapping("/user/resource")
@Tag(name = "resource", description = "资源模块")
public class ResourceController {

    @Autowired
    private ResourceApplication resourceApplication;

    /**
     * 获取资源列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SaResult list(@RequestParam(required = false) String id, @RequestParam(required = false) String mainId, @RequestParam(required = false) Integer mainType, @RequestParam(required = false) String slaveId, @RequestParam(required = false) Integer slaveType, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        Map<String, Object> result = resourceApplication.getResourceList(id, mainId, mainType, slaveId, slaveType, pageIndex, pageSize);
        return SaResult.data(result);
    }

    /**
     * 获取资源信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SaResult get(@PathVariable String id) {
        ResourceModel model = resourceApplication.getById(id);
        return SaResult.data(model);
    }

    /**
     * 添加资源
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public SaResult add(String mainId, int mainType, String slaveId, int slaveType) {
        return resourceApplication.addResource(mainId, mainType, slaveId, slaveType);
    }

    /**
     * 删除资源
     */
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public SaResult delete(@PathVariable String[] ids) {
        boolean b = resourceApplication.removeBatchByIds(Arrays.asList(ids));
        if (b) {
            return SaResult.ok("资源删除成功！");
        } else {
            return SaResult.error("资源删除失败！");
        }
    }

    /**
     * 根据用户删除资源
     */
    @RequestMapping(value = "/forMain", method = RequestMethod.DELETE)
    public SaResult delete(String mainId, Integer mainType) {
        return resourceApplication.forMain(mainId, mainType);
    }

    /**
     * 更新资源信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public SaResult update(@PathVariable String id, String mainId, Integer mainType, String slaveId, Integer slaveType) {
        return resourceApplication.updateResource(id, mainId, mainType, slaveId, slaveType);
    }
}
