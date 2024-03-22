package com.wbs.common.extend;

import com.wbs.common.enums.HttpEnum;

import java.io.Serializable;

/**
 * @author WBS
 * @date 2023/2/23 9:42
 * @desciption ResponseResult
 */
public class ResponseResult implements Serializable {
    private String message;
    private Object data;
    private int responseCode;
    private String sysCode;
    private boolean status;

    public ResponseResult() {
    }

    public ResponseResult(boolean status, String message, Object data, int responseCode, String sysCode) {
        this.setStatus(status);
        this.setResponseCode(responseCode);
        this.setSysCode(sysCode);
        this.setData(data);
        this.setMessage(message);
    }

    public ResponseResult OK(String message, Object data) {
        return new ResponseResult(true, message, data, HttpEnum.SUCCESS.getResponseCode(), null);
    }

    public ResponseResult OK() {
        return OK(HttpEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 处理成功
     *
     * @param data
     * @return
     */
    public ResponseResult OK(Object data) {
        return OK(HttpEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 处理成功
     *
     * @param message
     * @return
     */
    public ResponseResult OK(String message) {
        return OK(message, null);
    }

    /**
     * 处理失败
     *
     * @return
     */
    public ResponseResult FAILED() {
        return ERROR(HttpEnum.FAILED);
    }

    /**
     * 处理错误
     *
     * @param message
     * @param e
     * @return
     */
    public ResponseResult ERROR(String message, HttpEnum e) {
        return new ResponseResult(false, message, null, e.getResponseCode(), e.getSysCode());
    }

    /**
     * 处理错误
     *
     * @return
     */
    public ResponseResult ERROR() {
        return ERROR(HttpEnum.SERVER_ERROR);
    }

    /**
     * 处理错误
     *
     * @param e
     * @return
     */
    public ResponseResult ERROR(HttpEnum e) {
        return ERROR(e.getMessage(), e);
    }

    /**
     * 无记录
     *
     * @return
     */
    public ResponseResult NULL() {
        return new ResponseResult(false, HttpEnum.NO_CONTENT.getMessage(), null, HttpEnum.NO_CONTENT.getResponseCode(), HttpEnum.NO_CONTENT.getSysCode());
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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
