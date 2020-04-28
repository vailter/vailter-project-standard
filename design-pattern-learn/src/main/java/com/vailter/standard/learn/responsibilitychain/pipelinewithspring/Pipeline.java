package com.vailter.standard.learn.responsibilitychain.pipelinewithspring;

public interface Pipeline {
    Pipeline fireTaskReceived();

    Pipeline fireTaskFiltered();

    Pipeline fireTaskExecuted();

    Pipeline fireAfterCompletion();
}
