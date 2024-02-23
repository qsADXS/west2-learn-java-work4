package com.qsadxs.project.Dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultMap implements Serializable {

    private int code;
    private String message;
    private Object data;

    public static ResultMap success(Object data) {
        return success(200, "success", data);
    }

    public static ResultMap fail(String msg) {
        return fail(400, msg, null);
    }

    public static ResultMap success(int code, String msg, Object data) {
        ResultMap resultMap = new ResultMap();
        resultMap.setCode(code);
        resultMap.setMessage(msg);
        resultMap.setData(data);
        return resultMap;
    }

    public static ResultMap fail (int code, String msg, Object data) {
        ResultMap resultMap = new ResultMap();
        resultMap.setCode(code);
        resultMap.setMessage(msg);
        resultMap.setData(data);
        return resultMap;
    }
}
