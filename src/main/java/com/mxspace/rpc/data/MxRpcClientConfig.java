package com.mxspace.rpc.data;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据配置信息类
 */
@Data
@Component
public class MxRpcClientConfig {

    @Value("${mxRpc.client.link.host:127.0.0.1}")
    private String serverHost;


    @Value("${mxRpc.client.link.port:8082}")
    private Integer serverPort;

    @Value("${mxRpc.client.link.password:123456789}")
    private String password;

    /**
     * 客户端ID
     */
    @Value("${mxRpc.client.id:11101}")
    private String clientId;

    /**
     * 提供服务名称
     */
    @Value("${mxRpc.client.serviceName:ALL}")
    private String serviceName;

    /**
     * 提供服务名称
     */
    @Value("${mxRpc.client.serviceVersion:}")
    private String serviceVersion;

    /**
     * 提供服务名称
     */
    @Value("${mxRpc.client.weight:1}")
    private Integer weight;

    /**
     * 打印日志
     */
    @Value("${mxRpc.client.debug:true}")
    private Boolean debug;

    /**
     * 最长等待时间
     */
    @Value("${mxRpc.client.maxWaitTime:60}")
    private Integer maxWaitTime;
}
