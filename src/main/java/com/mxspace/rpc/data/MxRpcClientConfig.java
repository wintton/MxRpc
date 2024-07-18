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

    @Value("${mx-rpc.client.link.host:127.0.0.1}")
    private String serverHost;


    @Value("${mx-rpc.client.link.port:8081}")
    private int serverPort;

    @Value("${mx-rpc.client.link.password:123456789}")
    private String password;

    /**
     * 客户端ID
     */
    @Value("${mx-rpc.client.id:11101}")
    private String clientId;

    /**
     * 提供服务名称
     */
    @Value("${mx-rpc.client.serviceName:ALL}")
    private String serviceName;

    /**
     * 提供服务名称
     */
    @Value("${mx-rpc.client.weight:1}")
    private Integer weight;
}
