package com.zero.wolf.greenroad.bean;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/7/5 10:08
 * @updateAuthor ${Author}
 * @updataTIme 2016/7/5
 * @updataDes ${描述更新内容}
 */
public class ActivationResult {
    public static final String SUCCESS_CODE = "Success";
    public static final String FAILD_CODE = "Failure";
    public static final String RETRY = "Retry";
    public static final String FAILD_CODE_INVALID_KEY = "Invalid Key";
    public static final String FAILD_CODE_USED_KEY = "Used Key";

    private String code;
    private String detail;
    private boolean PostLogResult;

    public boolean isPostLogResult() {
        return PostLogResult;
    }

    public void setPostLogResult(boolean postLogResult) {
        PostLogResult = postLogResult;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
