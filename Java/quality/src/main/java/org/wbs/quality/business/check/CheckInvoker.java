package org.wbs.quality.business.check;

import org.wbs.quality.business.check.enums.CompareEnum;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WBS
 */
public class CheckInvoker {
    public Set<DataCheckBase> list = new HashSet<>();

    public CheckInvoker(DataCheckBase command) {
        this.list.add(command);
    }

    public CheckInvoker(Set<DataCheckBase> list) {
        this.list.addAll(list);
    }

    /**
     * 命令入口
     */
    public void action(BigDecimal value, CompareEnum e) {
        for (DataCheckBase command : list) {
            boolean state = command.execute(value, e);
            if (state) {
                return;
            }
        }
//        System.out.println("数据正常");
    }
}
