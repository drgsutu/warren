package io.sutu.warren.storage;

import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class CsvFileWriterTaskFactory {

    private PipelineQueuesFactory pipelineQueuesFactory;

    public CsvFileWriterTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public CsvFileWriterTask newTask() {
        return new CsvFileWriterTask(pipelineQueuesFactory.getOHLCVToBeStoredQueue());
    }
}
