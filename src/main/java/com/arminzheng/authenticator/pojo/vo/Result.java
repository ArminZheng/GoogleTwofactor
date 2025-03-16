package com.arminzheng.authenticator.pojo.vo;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Result<T> implements Serializable {

    public static final String OPS_SUCCESS = "OK!";
    private static final long serialVersionUID = 1L;
    private final long timestamp = System.currentTimeMillis();

    private Boolean success = true;

    private String message = OPS_SUCCESS;

    private Integer code = 0;

    private T data;

    public static <T> Result<T> ok(T data) {

        return ok(OPS_SUCCESS, data);
    }

    public static <T> Result<T> ok() {

        return ok(OPS_SUCCESS);
    }

    public static <T> Result<T> ok(String msg) {

        return ok(msg, null);
    }

    public static <T> Result<T> ok(String msg, T data) {

        Result<T> r = new Result<>();
        r.setMessage(msg);
        r.setCode(200);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> error(String msg) {

        return error(500, msg);
    }

    public static <T> Result<T> error(int code, String msg) {

        return error(code, msg, null);
    }

    public static <T> Result<T> error(String msg, T data) {

        return error(500, msg, data);
    }

    public static <T> Result<T> error(int code, String msg, T data) {

        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setMessage(msg);
        r.setCode(code);
        r.setData(data);
        return r;
    }

}
