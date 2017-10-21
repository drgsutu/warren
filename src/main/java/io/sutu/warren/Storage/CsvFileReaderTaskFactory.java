package io.sutu.warren.Storage;

import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class CsvFileReaderTaskFactory {

    private PipelineQueuesFactory pipelineQueuesFactory;

    public CsvFileReaderTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public CsvFileReaderTask newTask() {
        return new CsvFileReaderTask(pipelineQueuesFactory.getOHLCVToBeProccessedQueue());
    }
}
