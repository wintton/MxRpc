package com.mxspace.rpc.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录对象
 */
@Data
public class MxRpcLogin implements Serializable {

    static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 提供的服务名称
     */
    private String serviceName;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 权重
     */
    private Integer weight = 1;

}
