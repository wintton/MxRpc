package com.mxspace.rpc.test.rest;

import com.mxspace.rpc.annotation.MxRpcResource;
import com.mxspace.rpc.test.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestRest {
    @MxRpcResource
    TestService testService;

    @GetMapping
    public String doQueryTest(){
        return testService.sayHello();
    }
}
