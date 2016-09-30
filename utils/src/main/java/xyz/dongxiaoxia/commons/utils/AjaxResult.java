package xyz.dongxiaoxia.commons.utils;

/**
 * Created by dongxiaoxia on 2016/9/30.
 * <p>
 * ajax返回封装
 */
public class AjaxResult {

    /**
     * 返回码
     */
    private int code;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回结果描述
     */
    private String message;
    /**
     * 返回数据内容
     */
    private Object data;

    public static AjaxResult success() {
        return success(null);
    }

    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(ResultStatus.SUCCESS.getCode());
        ajaxResult.setData(data);
        ajaxResult.setSuccess(true);
        return ajaxResult;
    }

    public static AjaxResult fail(String message) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setSuccess(false);
        ajaxResult.setCode(-1);
        ajaxResult.setMessage(message);
        return ajaxResult;
    }

    public AjaxResult() {
    }

    public AjaxResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = "";
    }

    public AjaxResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public AjaxResult(ResultStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = "";
    }

    public AjaxResult(ResultStatus status, Object data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    public static AjaxResult ok(Object data) {
        return new AjaxResult(ResultStatus.SUCCESS, data);
    }

    public static AjaxResult ok() {
        return new AjaxResult(ResultStatus.SUCCESS);
    }

    public static AjaxResult error(ResultStatus error) {
        return new AjaxResult(error);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
}
