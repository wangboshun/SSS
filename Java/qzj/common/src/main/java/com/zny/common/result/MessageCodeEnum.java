package com.zny.common.result;

/**
 * 错误码类
 * 组成：应用标识+功能模块+错误类型+错误编码
 * <p>
 * 应用标识(1位字母)
 * <p>
 * ##例如:用户中心: U; ERP系统:E
 * <p>
 * 功能模块(2位数字)
 * <p>
 * ##例如:未分类:00;用户登录鉴权:01;
 * <p>
 * 错误类型(1位字母)
 * <p>
 * ##例如:认证授权:A;业务错误:B;缓存错误:C﹔数据库错误:D;文件IO错误:F;网络错误:N;其他错误:O;参数错误:P;RPC错误:R;系统错误:S;
 * <p>
 * 错误编码(2位数字)
 * <p>
 * ##自增即可，每个模块的每种错误类型可以注册100个错误，如果使用合理的话，是完全够用的
 * <p>
 * 错误码位数：6位
 * <p>
 * 使用规范：只增不改
 * <p>
 * 正常情况返回： 000000
 * <p>
 * 1xx指示信息，服务器收到请求，需要请求者继续执行操作
 * <p>
 * 2xx请求成功，操作被成功接收并处理
 * <p>
 * 3xx重定向，需要进—步的操作以完成请求
 * <p>
 * 4xx客户端错误，请求包含语法错误或无法完成请求
 * <p>
 * 5xx服务器错误，服务器在处理请求的过程中发生了错误
 *
 * @author WBS
 * Date:2022/9/28
 */

public enum MessageCodeEnum {

    /**
     * 成功
     */
    SUCCESS("000000", 200, "成功"),

    /**
     * 参数校验(Valid)异常
     */
    PARAM_VALID_ERROR("U00001", 400, "参数校验异常"),

    /**
     * Token已过期
     */
    TOKEN_EXPIRED("U00002", 401, "Token已过期"),

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
    SERVER_BUSY("S000001", 500, "服务器繁忙"),

    /**
     * 未知异常，无法识别的异常
     */
    SERVER_ERROR("S00O02", 500, "未知异常"),


    /**
     * 数据库操作失败
     */
    DB_ERROR("D00O01", 500, "数据库操作失败"),


    /**
     * 抛出异常
     */
    EXCEPTION("E00O01", 500, "抛出异常"),
    ;

    public String code;
    public Integer responseCode;
    public String msg;

    MessageCodeEnum(String code, Integer responseCode, String msg) {
        this.code = code;
        this.responseCode = responseCode;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
