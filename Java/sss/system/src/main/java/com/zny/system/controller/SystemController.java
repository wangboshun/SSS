package com.zny.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.zny.system.application.system.SystemApplication;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WBS
 * Date:2023/02/09
 * api日志控制器
 */

@RestController
@RequestMapping("/system/sys/")
@Tag(name = "system", description = "系统模块")
public class SystemController {

    private final SystemApplication systemApplication;

    public SystemController(SystemApplication systemApplication) {
        this.systemApplication = systemApplication;
    }

    /**
     * 查看程序占用线程
     */
    @GetMapping(value = "/threads")
    public SaResult threads() {
        return systemApplication.getCurrentProcessThreads();
    }

    /**
     * 获取主机系统信息
     */
    @GetMapping(value = "/info")
    public SaResult info() {
        return systemApplication.getSystemInfo();
    }
}
