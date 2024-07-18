package com.mxspace.rpc.rest;

import com.mxspace.rpc.annotation.MxRpcResource;
import com.mxspace.rpc.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class TestRest {

    @MxRpcResource
    TestService testService;

    @GetMapping
    public String getResult(){
        testService.queryDataInfo("你好");
        return  "0";
    }

}
