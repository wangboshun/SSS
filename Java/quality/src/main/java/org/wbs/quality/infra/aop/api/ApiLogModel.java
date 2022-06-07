package org.wbs.quality.infra.aop.api;

import java.util.Map;

/**
 * @author WBS
 * Date:2022/6/6
 */

public class ApiLogModel {

    @lombok.Setter
    private String name;

    @lombok.Setter
    private Long time;

    @lombok.Setter
    private Object result;

    @lombok.Setter
    private String client;

    @lombok.Setter
    private String url;

    @lombok.Setter
    private String method;

    @lombok.Setter
    private Map<String, Object> parameters;

}