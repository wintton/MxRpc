package com.mxspace.rpc.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 记录任务处理对象
 */
@Data
public class MxRpcHandleObj implements Serializable {

    static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求对象ID
     */
    private String requestCtxId;

    /**
     * 回复对象ID
     */
    private String responseCtxId;

}
