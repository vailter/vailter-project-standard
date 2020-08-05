package com.vailter.standard.learn.responsibilitychain.demo4;
/**
 * 责任链模式：处理每个模块阀门
 */
public interface Valve {
    /**
     * 调用
     * @param netCheckDTO
     */
    void invoke(NetCheckDTO netCheckDTO);
}
