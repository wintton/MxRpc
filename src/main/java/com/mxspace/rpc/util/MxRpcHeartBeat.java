package com.mxspace.rpc.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 心跳包
 */
@Data
public class MxRpcHeartBeat implements Serializable {

    static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 服务名称
     */
    private String serviceName;

}
