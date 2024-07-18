package com.mxspace.rpc;

import com.mxspace.rpc.annotation.EnableMxRpcClient;
import com.mxspace.rpc.annotation.EnableMxRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMxRpcClient
@EnableMxRpcServer
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
