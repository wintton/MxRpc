package com.mxspace.rpc.util;

import lombok.Data;

/**
 * 请求对象
 */
@Data
public class MxRpcRequest {

    static final long serialVersionUID = 1L;

    /**
     * 请求对象的ID
     */
    private String requestId;

    /**
     * 请求提供的服务类
     */
    private String serviceName;

    /**
     * 请求提供的服务类版本号 不给就匹配所有
     */
    private String serviceVersion;

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
