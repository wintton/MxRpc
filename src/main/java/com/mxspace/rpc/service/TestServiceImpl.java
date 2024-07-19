package com.mxspace.rpc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Override
    public int queryDataInfo() {
        return 0;
    }

    @Override
    public void queryDataInfo(String info) {
        log.info(info);
    }
}
