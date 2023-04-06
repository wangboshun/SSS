package com.wbs.common.enums;

/**
 * @author WBS
 * @date 2023/3/9 8:38
 * @desciption HttpEnum
 */
public enum HttpEnum {
    /**
     * 成功
     */
    SUCCESS("000000", 200, "成功"),

    /**
     * 成功
     */
    NO_CONTENT("000004", 204, "无数据"),

    /**
     * 参数校验(Valid)异常
     */
    PARAM_VALID_ERROR("U00000", 400, "参数校验异常"),

    /**
     * 数据已存在
     */
    EXISTS("U000009", 400, "数据已存在"),

    /**
     * 操作不成功
     */
    FAILED("U000008", 400, "失败"),

    /**
     * Token已过期
     */
    TOKEN_EXPIRED("U00001", 401, "Token已过期"),

    /**
     * 未授权，不能访问
     */
    AUTH_INVALID("U00003", 403, "未授权，不能访问"),

    /**
     * 资源找不到
     */
    NOT_FOUND("U00004", 404, "资源未找到"),

    /**
     * 服务器繁忙，请稍后重试
     */
    SERVER_BUSY("S000000", 500, "服务器繁忙"),

    /**
     * 未知异常，无法识别的异常
     */
    SERVER_ERROR("S00O01", 500, "未知异常"),


    /**
     * 数据库操作失败
     */
    DB_ERROR("D00O00", 500, "数据库操作失败"),


    /**
     * 抛出异常
     */
    EXCEPTION("E00O01", 500, "抛出异常");

    private String sysCode;
    private Integer responseCode;
    private String message;

    HttpEnum(String sysCode, Integer responseCode, String message) {
        this.sysCode = sysCode;
        this.responseCode = responseCode;
        this.message = message;
    }

    public String getSysCode() {
        return sysCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }
}
