package com.hnyr.sys.config;

/**
 * @ClassName: BusinessException
 * @Description: 统一自定义业务异常
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class BusinessException extends RuntimeException {

    private int code;
    private String exception;

    /**
     * 业务异常
     *
     * @param message 信息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ErrorEnum.RUNTIME_EXCEPTION.getCode();
    }

    /**
     * 业务异常
     *
     * @param code    编码
     * @param message 信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.exception = this.getClass().getName();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
