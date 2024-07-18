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


    @Value("${mx-rpc.server.port:8081}")
    private int port;


    @Value("${mx-rpc.server.password:123456789}")
    private String password;
}
