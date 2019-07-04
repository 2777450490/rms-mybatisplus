package com.unis.crk.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 *  响应客户端对象封装
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultUtil<T> implements Serializable{

    private Integer status;
    private String message;
    private T data = null;

    public ResultUtil<T> success(){
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        return this;
    }

    public ResultUtil<T> success(String message){
        this.status = HttpStatus.OK.value();
        this.message = message;
        return this;
    }

    public ResultUtil<T> success(T t){
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        this.data = t;
        return this;
    }

    public ResultUtil<T> success(String message,T t){
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.data = t;
        return this;
    }

    public ResultUtil<T> failed(){
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = HttpStatus.BAD_REQUEST.getReasonPhrase();
        return this;
    }

    public ResultUtil<T> failed(String message){
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = message;
        return this;
    }

    public ResultUtil<T> failed(Integer status,String message){
        this.status = status;
        this.message = message;
        return this;
    }

    public ResultUtil<T> noAuth(){
        this.status = HttpStatus.UNAUTHORIZED.value();
        this.message = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        return this;
    }

    public ResultUtil<T> noAuth(String message){
        this.status = HttpStatus.UNAUTHORIZED.value();
        this.message = message;
        return this;
    }
}
