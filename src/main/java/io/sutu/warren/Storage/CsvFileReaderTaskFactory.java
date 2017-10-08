package io.sutu.warren.Storage;

import io.sutu.warren.PipelineQueuesFactory;
import io.sutu.warren.Trade;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class CsvFileReaderTaskFactory {

    private final BlockingQueue<Trade> tradesToBeAggregatedQueue;

    public CsvFileReaderTaskFactory(PipelineQueuesFactory pipelineQueuesFactory) {
        this.tradesToBeAggregatedQueue = pipelineQueuesFactory.getTradesToBeAggregatedQueue();
    }

    public CsvFileReaderTask newTask() {
        return new CsvFileReaderTask(tradesToBeAggregatedQueue);
    }
}
