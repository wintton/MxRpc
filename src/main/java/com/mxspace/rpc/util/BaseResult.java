package com.mxspace.rpc.util;

import lombok.Data;

/**
 * 基础数据返回类
 */
@Data
public class BaseResult {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 201;

    /**
     * 返回编码
     */
    private Integer code;

    /**
     * 返回描述
     */
    private String desc;

    /**
     * 返回数据内容
     */
    private Object data;

    public static BaseResult success(){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(SUCCESS_CODE);
        baseResult.setDesc("操作成功");
        return baseResult;
    }

    public static BaseResult success(Object data){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(SUCCESS_CODE);
        baseResult.setDesc("操作成功");
        baseResult.setData(data);
        return baseResult;
    }

    public static BaseResult fail(){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(FAIL_CODE);
        baseResult.setDesc("操作失败");
        return baseResult;
    }

    public static BaseResult fail(String desc){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(FAIL_CODE);
        baseResult.setDesc(desc);
        return baseResult;
    }
}
