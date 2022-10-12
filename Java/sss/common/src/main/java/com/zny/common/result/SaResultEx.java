package com.zny.common.result;

import cn.dev33.satoken.util.SaResult;

/**
 * @author WBS
 * Date:2022/9/28
 */

public class SaResultEx extends SaResult {
    public static SaResult error(MessageCodeEnum e) {
        return new SaResult(e.getResponseCode(), e.getMsg(), e.getCode());
    }

    public static SaResult error(MessageCodeEnum e, String msg) {
        return new SaResult(e.getResponseCode(), e.getMsg() + "(" + msg + ")", e.getCode());
    }
}
