package com.mxspace.rpc.util;

import lombok.Data;

/**
 * 封装的响应对象
 */
@Data
public class MxRpcResponse {

    static final long serialVersionUID = 1L;

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
    private BaseResult result;

}
