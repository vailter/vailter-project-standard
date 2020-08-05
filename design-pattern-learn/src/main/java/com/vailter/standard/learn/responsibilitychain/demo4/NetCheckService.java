package com.vailter.standard.learn.responsibilitychain.demo4;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NetCheckService {
    // 注入所有的阀门
    @Autowired
    private Map<String, Valve> valveMap;

    /**
     * 发送查验请求
     *
     * @param netCheckDTO
     */
    @Async("asyncExecutor")
    public void sendCheckRequest(NetCheckDTO netCheckDTO) {
        // 用于保存客户选择处理的模块阀门
        List<Valve> valves = new ArrayList<>();

        CheckModuleConfigDTO checkModuleConfig = netCheckDTO.getCheckModuleConfigDTO();
        // 将用户选择查验的模块添加到 阀门链条中
        if (checkModuleConfig.getBaiduNegative()) {
            valves.add(valveMap.get("baiduNegativeValve"));
        }
        // 省略部分代码.......
        if (CollectionUtils.isEmpty(valves)) {
            log.info("网查查验模块为空，没有需要查验的任务");
            return;
        }
        // 触发处理
        valves.forEach(valve -> valve.invoke(netCheckDTO));
    }
}
