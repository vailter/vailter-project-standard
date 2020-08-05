package com.vailter.standard.learn.responsibilitychain.demo4;

public abstract class AbstractCheckValve implements Valve {
    public final AnalysisReportLogDO getLatestHistoryData(NetCheckDTO netCheckDTO, NetCheckDataTypeEnum checkDataTypeEnum){
        // 获取历史记录，省略代码逻辑
        return null;
    }

    // 获取查验数据源配置
    public final String getModuleSource(String querySource, ModuleEnum moduleEnum){
        // 省略代码逻辑
        return null;
    }
}
