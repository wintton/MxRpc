package com.mxspace.rpc.test;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService{
    @Override
    public String sayHello() {
        return "Hello World";
    }
}
