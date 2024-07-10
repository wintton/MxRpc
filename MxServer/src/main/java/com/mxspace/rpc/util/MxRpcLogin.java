package com.mxspace.rpc.util;

import lombok.Data;

/**
 * 登录对象
 */
@Data
public class MxRpcLogin {

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

}
