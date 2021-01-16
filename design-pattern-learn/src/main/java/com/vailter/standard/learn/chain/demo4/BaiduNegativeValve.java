package com.vailter.standard.learn.chain.demo4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BaiduNegativeValve extends AbstractCheckValve {
    @Override
    public void invoke(NetCheckDTO netCheckDTO) {
        System.out.println("baidu...");
    }
}
