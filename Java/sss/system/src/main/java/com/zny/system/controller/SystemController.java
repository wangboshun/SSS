package com.zny.system.controller;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author WBS
 * Date:2023/02/09
 * api日志控制器
 */

@RestController
@RequestMapping("/system/sys/")
@Tag(name = "system", description = "系统模块")
public class SystemController {

    /**
     * 查看程序线程
     */
    @GetMapping(value = "/threads")
    public SaResult threads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (ThreadInfo threadInfo : threadInfos) {
            HashMap<String, String> map = new HashMap<>(3);
            map.put("id", threadInfo.getThreadId() + "");
            map.put("name", threadInfo.getThreadName());
            map.put("status", threadInfo.getThreadState().toString());
            list.add(map);
        }
        HashMap<String, Object> result = new HashMap<>(2);
        result.put("count", threadInfos.length);
        result.put("threads", list);
        return SaResult.data(result);
    }
}
