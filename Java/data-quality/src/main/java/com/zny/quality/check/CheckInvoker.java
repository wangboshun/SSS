package com.zny.quality.check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WBS
 */
public class CheckInvoker {
    public List<DataCheckAbstract> list = new ArrayList<>();

    public CheckInvoker(DataCheckAbstract command) {
        this.list.add(command);
    }

    public CheckInvoker(List<DataCheckAbstract> list) {
        this.list.addAll(list);
    }

    public void action(BigDecimal value, CompareEnum e) {
        for (DataCheckAbstract command : list) {
            boolean state = command.execute(value, e);
            if (state) {
                return;
            }
        }
    }
}
