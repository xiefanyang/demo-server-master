package com.hnyr.sys.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @ClassName: ResponseResult
 * @Description: 统一rest响应数据包封装
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class ResponseResult<T> implements Serializable {
    private long time;
    /**
     * 请求处理是否成功
     */
    private boolean success;

    /**
     * 编码
     */
    private int code = 0;

    /**
     * 消息
     */
    private String message = "";

    /**
     * 响应内容实体
     */
    protected T data;
    @JsonIgnore
    private String exception;
    @JsonIgnore
    private Throwable throwable;

    public ResponseResult() {
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    protected ResponseResult(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = (int) (System.currentTimeMillis() / 1000);
        if (this.success) {
            this.code = 0;
            this.message = "";
        }
    }

    protected ResponseResult(boolean success, int code, String message, T data, String exception, Throwable throwable) {
        this.code = code;
        this.success = success;
        this.message = message;
        if (this.success) {
            this.code = 0;
            this.message = "";
        }

        this.data = data;
        this.exception = exception;
        this.throwable = throwable;
        this.time = (int) (System.currentTimeMillis() / 1000);
        if (this.success != true) {
            if (throwable == null || !(throwable instanceof BusinessException)) {
            } else {
                this.message = throwable.getMessage();
            }
        }
    }


    /**
     * 成功
     */
    public static ResponseResult success() {
        return new ResponseResult<>(true, 0, null, null);
    }

    /**
     * 成功
     */
    public static <T> ResponseResult success(T data) {
        return new ResponseResult<>(true, 0, null, data);
    }

    /**
     * 失败
     */
    public static ResponseResult error(int errCode, String message) {
        return new ResponseResult<>(false, errCode, message, null);
    }

    /**
     * 失败
     */
    public static <T> ResponseResult<T> error(int errCode, String message, String exception, Throwable throwable) {
        return new ResponseResult<>(false, errCode, message, null, exception, throwable);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        if (data == null || !StringUtils.hasText(data.toString())) {
            return (T) new HashMap<String, Object>();
        }
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
