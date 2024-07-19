package com.mxspace.rpc.data;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据配置信息类
 */
@Data
@Component
public class MxRpcServerConfig {


    @Value("${mxRpc.server.port:8081}")
    private Integer port;


    @Value("${mxRpc.server.password:123456789}")
    private String password;

    /**
     * 打印日志
     */
    @Value("${mxRpc.server.debug:true}")
    private Boolean debug;



}
