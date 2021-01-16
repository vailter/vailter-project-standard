package com.vailter.standard.learn.chain.pipelinewithspring;

public interface Pipeline {
    Pipeline fireTaskReceived();

    Pipeline fireTaskFiltered();

    Pipeline fireTaskExecuted();

    Pipeline fireAfterCompletion();
}
