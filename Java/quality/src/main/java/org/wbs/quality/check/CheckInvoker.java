package org.wbs.quality.check;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WBS
 */
public class CheckInvoker {
    public Set<AbstractDataCheck> list = new HashSet<>();

    public CheckInvoker(AbstractDataCheck command) {
        this.list.add(command);
    }

    public CheckInvoker(Set<AbstractDataCheck> list) {
        this.list.addAll(list);
    }

    /**
     * 命令入口
     */
    public void action(BigDecimal value, CompareEnum e) {
        for (AbstractDataCheck command : list) {
            boolean state = command.execute(value, e);
            if (state) {
                return;
            }
        }
//        System.out.println("数据正常");
    }
}
