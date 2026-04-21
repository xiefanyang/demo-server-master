package com.hnyr.sys.login.vo;

import com.hnyr.sys.config.BusinessException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
/**
 * @ClassName: OAuth2ResponseResult
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
public class OAuth2ResponseResult<T> implements Serializable {

    public static final Integer APP_RESULT_CODE_SUCCESS = 0;
    public static final String APP_RESULT_CODE_SUCCESS_DESC = "成功";
    public static final Integer APP_NO_AUTH = -1;
    public static final Integer APP_RESULT_CODE_BIS = -4;
    public static final Integer APP_RESULT_CODE_FAILED = -99;

    //以下3个字段为兼容B端原有APP数据获取展示模式
    private Integer code;
    private String desc;
    private Integer time;

    private String rpcMsg;
    /**
     * 请求处理是否成功
     */
    private boolean success;

    /**
     * 错误编码
     */
    private String errCode;

    /**
     * 错误消息
     */
    private String errMsg;

    /**
     * 响应内容实体
     */
    protected T data;

    private String exception;

    private Throwable throwable;

    protected OAuth2ResponseResult() {
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getRpcMsg() {
        return rpcMsg;
    }

    public void setRpcMsg(String rpcMsg) {
        this.rpcMsg = rpcMsg;
    }

    protected OAuth2ResponseResult(boolean success, String errCode, String errMsg, T data) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.desc = errMsg;
        this.data = data;
        this.time = (int) (System.currentTimeMillis() / 1000);
        if (this.success == true) {
            this.code = APP_RESULT_CODE_SUCCESS;
            this.desc = APP_RESULT_CODE_SUCCESS_DESC;
        } else {
            this.code = APP_RESULT_CODE_BIS;
        }
    }

    protected OAuth2ResponseResult(boolean success, String errCode, String errMsg, T data, String exception, Throwable throwable) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.desc = errMsg;
        this.data = data;
        this.exception = exception;
        this.throwable = throwable;
        this.time = (int) (System.currentTimeMillis() / 1000);
        if (this.success == true) {
            this.code = APP_RESULT_CODE_SUCCESS;
            this.desc = APP_RESULT_CODE_SUCCESS_DESC;
        } else {
            if (throwable == null || throwable instanceof BusinessException) {
                this.code = APP_RESULT_CODE_BIS;
            } else {
                this.code = APP_RESULT_CODE_FAILED;
                this.errMsg = throwable.getMessage();
            }
        }
    }

    protected OAuth2ResponseResult(boolean success, String errCode, String errMsg, T data, String exception, Integer code, Throwable throwable, String rpcMsg) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.desc = errMsg;
        this.data = data;
        this.exception = exception;
        this.throwable = throwable;
        this.rpcMsg = rpcMsg;
        this.time = (int) (System.currentTimeMillis() / 1000);
        if (code != null) {
            this.code = code;
        } else {
            if (this.success == true) {
                this.code = APP_RESULT_CODE_SUCCESS;
                this.desc = APP_RESULT_CODE_SUCCESS_DESC;
            } else {
                if (throwable == null || throwable instanceof BusinessException) {
                    this.code = APP_RESULT_CODE_BIS;
                } else {
                    this.code = APP_RESULT_CODE_FAILED;
                    this.errMsg = throwable.getMessage();
                }
            }
        }
    }


    /**
     * 成功
     */
    public static OAuth2ResponseResult success() {
        return new OAuth2ResponseResult<>(true, null, null, null);
    }

    /**
     * 成功
     */
    public static <T> OAuth2ResponseResult success(T data) {
        return new OAuth2ResponseResult<>(true, null, null, data);
    }

    /**
     * 失败
     */
    public static OAuth2ResponseResult error(String errCode, String errMsg) {
        return new OAuth2ResponseResult<>(false, errCode, errMsg, null);
    }

    /**
     * 失败
     */
    public static <T> OAuth2ResponseResult<T> error(String errCode, String errMsg, String exception, Throwable throwable) {
        return new OAuth2ResponseResult<>(false, errCode, errMsg, null, exception, throwable);
    }

    /**
     * 失败
     */
    public static <T> OAuth2ResponseResult<T> error(String errCode, String errMsg, String exception, Integer code, Throwable throwable) {
        return new OAuth2ResponseResult<>(false, errCode, errMsg, null, exception, code, throwable, null);
    }

    /**
     * 失败
     */
    public static <T> OAuth2ResponseResult<T> error(String errCode, String errMsg, String exception, Integer code, Throwable throwable, String rpcMsg) {
        return new OAuth2ResponseResult<>(false, errCode, errMsg, null, exception, code, throwable, rpcMsg);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
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

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        String start = "RPC:";
        if (StringUtils.hasText(desc) && desc.startsWith(start)) {
            return desc.substring(4);
        }
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}