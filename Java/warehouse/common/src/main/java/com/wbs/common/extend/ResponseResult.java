package com.wbs.common.extend;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption ResponseResult
 */
public class ResponseResult implements Serializable {
    public String message;
    public Object data;
    public int code;
    public boolean status;

    public ResponseResult() {
    }

    public ResponseResult(String message, Object data, int code, boolean status) {
        this.message = message;
        this.data = data;
        this.code = code;
        this.status = status;
    }

    public ResponseResult Ok(Object data) {
        return new ResponseResult("请求成功", data, 200, true);
    }

    public ResponseResult Ok(String message, Object data) {
        return new ResponseResult(message, data, 200, true);
    }

    public ResponseResult Error(Object data) {
        return new ResponseResult("请求失败", data, 500, false);
    }

    public ResponseResult Error(String message, Object data) {
        return new ResponseResult(message, data, 500, false);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
