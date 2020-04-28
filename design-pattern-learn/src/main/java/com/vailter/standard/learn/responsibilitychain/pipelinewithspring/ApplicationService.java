package com.vailter.standard.learn.responsibilitychain.pipelinewithspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationContext context;

    public void mockedClient() {
        Request request = new Request();  // request一般是通过外部调用获取
        Pipeline pipeline = newPipeline(request);
        try {
            pipeline.fireTaskReceived();
            pipeline.fireTaskFiltered();
            pipeline.fireTaskExecuted();
        } finally {
            pipeline.fireAfterCompletion();
        }
    }

    private Pipeline newPipeline(Request request) {
        return context.getBean(DefaultPipeline.class, request);
    }
}
