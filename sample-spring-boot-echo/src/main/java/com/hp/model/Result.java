package com.hp.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.enumerate.ResultCode;

import com.hp.util.ModelObjectMapper;
import lombok.Builder;
import lombok.Data;


@Data
public class Result {

    private Integer code;

    private String msg;

    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     *
     * @param resultCode
     * @return
     */
    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    /**
     *
     * @param resultCode
     * @param data
     * @return
     */
    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    private void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

}
