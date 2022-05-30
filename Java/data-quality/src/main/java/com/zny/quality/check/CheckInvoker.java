package com.zny.quality.check;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WBS
 */
public class CheckInvoker {
    public Set<DataCheckAbstract> list = new HashSet<>();

    public CheckInvoker(DataCheckAbstract command) {
        this.list.add(command);
    }

    public CheckInvoker(Set<DataCheckAbstract> list) {
        this.list.addAll(list);
    }

    /**
     * 命令入口
     */
    public void action(BigDecimal value, CompareEnum e) {
        for (DataCheckAbstract command : list) {
            boolean state = command.execute(value, e);
            if (state) {
                return;
            }
        }
        System.out.println("数据正常");
    }
}
