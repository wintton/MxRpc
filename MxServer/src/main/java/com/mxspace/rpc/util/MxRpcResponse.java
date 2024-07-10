package com.mxspace.rpc.util;

import lombok.Data;

/**
 * 封装的响应对象
 */
@Data
public class MxRpcResponse {

    /**
     * 响应ID
     */
    private String requestId;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 返回的结果
     */
    private Object result;

}
