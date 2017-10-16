package com.myshop.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by chs on 2017-10-13.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//序列化json的时候如果为null则会消失
public class ServerResponse<T> implements Serializable {
    private int resCode;
    private String msg;
    private  T data;

    private ServerResponse(int resCode) {
        this.resCode = resCode;
    }

    private ServerResponse(int resCode, T data) {
        this.resCode = resCode;
        this.data = data;
    }

    private ServerResponse(int resCode, String msg, T data) {
        this.resCode = resCode;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int resCode, String msg) {
        this.resCode = resCode;
        this.msg = msg;
    }

    @JsonIgnore//不在json序列化当中
    public boolean isSuccess(){
        return resCode == ResponseCode.SUCCESS.getCode();
    }

    public int getResCode() {
        return resCode;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
