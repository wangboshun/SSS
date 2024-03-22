package com.zny.system.application.system;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import org.springframework.stereotype.Service;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;

@Service
public class SystemApplication {

    public SaResult getCurrentProcessThreads() {
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

    public SaResult getSystemInfo() {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        GlobalMemory memory = OshiUtil.getMemory();
        List<HWDiskStore> diskStores = OshiUtil.getDiskStores();

        OperatingSystem os = OshiUtil.getOs();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("os", os.toString());

        result.put("memory_total", convertSize(memory.getTotal()));
        result.put("memory_use", convertSize((memory.getTotal())));

        result.put("cpu_use", cpuInfo.getUsed());
        result.put("cpu_free", cpuInfo.getFree());
        result.put("cpu_info", cpuInfo.getCpuModel().split("\n")[0]);

        List<Map<String, Object>> diskMap = new ArrayList<>();
        for (HWDiskStore disk : diskStores) {
            for (HWPartition part : disk.getPartitions()) {
                Map<String, Object> panMap = new LinkedHashMap<>();
                File pan = new File(part.getMountPoint());
                String totalSpace = convertSize(pan.getTotalSpace());
                String freeSpace = convertSize(pan.getFreeSpace());
                panMap.put("name", part.getMountPoint());
                panMap.put("total", totalSpace);
                panMap.put("free", freeSpace);
                diskMap.add(panMap);
            }
        }
        result.put("disk", diskMap);
        return SaResult.data(result);
    }

    private String convertSize(long size) {
        return convertSize(size, "GB");
    }

    private String convertSize(long size, String unit) {
        if ("KB".equals(unit)) {
            return String.format("%.1f", (Double.parseDouble(size + "") / 1024)) + "K";
        } else if ("MB".equals(unit)) {
            return String.format("%.1f", (Double.parseDouble(size + "") / 1024 / 1024)) + "M";
        } else if ("GB".equals(unit)) {
            return String.format("%.1f", (Double.parseDouble(size + "") / 1024 / 1024 / 1024)) + "G";
        }
        return "";
    }
}
