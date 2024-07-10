package com.mxspace.rpc.util;

import lombok.Data;

/**
 * 请求对象
 */
@Data
public class MxRpcRequest {

    /**
     * 请求对象的ID
     */
    private String requestId;

    /**
     * 请求提供的服务类
     */
    private String serviceName;

    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 入参
     */
    private Object[] parameters;

}
