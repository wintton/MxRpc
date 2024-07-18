package com.mxspace.rpc;

import com.mxspace.rpc.annotation.EnableMxRpcClient;
import com.mxspace.rpc.annotation.EnableMxRpcServer;
import com.mxspace.rpc.enums.ProviderVisitStrategyEnums;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMxRpcClient
@EnableMxRpcServer(visitStrategy = ProviderVisitStrategyEnums.NEXT)
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
