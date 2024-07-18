package com.mxspace.rpc.service;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public int queryDataInfo() {
        return 0;
    }

    @Override
    public void queryDataInfo(String info) {
        System.out.println(info);
    }
}
