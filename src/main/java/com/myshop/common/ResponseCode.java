package com.myshop.common;

/**
 * Created by chs on 2017-10-13.
 */
public enum  ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),//需要登陆
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");//错误状态

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
