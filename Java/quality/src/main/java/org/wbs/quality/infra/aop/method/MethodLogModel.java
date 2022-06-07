package org.wbs.quality.infra.aop.method;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/6/6
 */

public class MethodLogModel {

    @lombok.Setter
    private String name;

    @lombok.Setter
    private Long time;

    @lombok.Setter
    private Object result;

    @lombok.Setter
    private Map<String, Object> parameters;

    @lombok.Setter
    private String attribute;
}