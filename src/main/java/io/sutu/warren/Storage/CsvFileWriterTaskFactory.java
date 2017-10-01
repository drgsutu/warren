package io.sutu.warren.Storage;

import io.sutu.warren.PipelineQueuesFactory;
import io.sutu.warren.Trade;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class CsvFileWriterTaskFactory {

    private final BlockingQueue<Trade> rawDataToBeStoredQueue;

    public CsvFileWriterTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.rawDataToBeStoredQueue = pipelineQueuesFactory.getRawDataToBeStoredQueue();
    }

    public CsvFileWriterTask newTask() {
        return new CsvFileWriterTask(rawDataToBeStoredQueue);
    }
}
