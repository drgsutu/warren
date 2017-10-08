package io.sutu.warren.Storage;

import io.sutu.warren.PipelineQueuesFactory;
import io.sutu.warren.Trade;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class CsvFileWriterTaskFactory {

    private final BlockingQueue<Trade> tradesToBeStoredQueue;

    public CsvFileWriterTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.tradesToBeStoredQueue = pipelineQueuesFactory.getTradesToBeStoredQueue();
    }

    public CsvFileWriterTask newTask() {
        return new CsvFileWriterTask(tradesToBeStoredQueue);
    }
}
