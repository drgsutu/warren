package io.sutu.warren.storage;

import com.typesafe.config.Config;
import io.sutu.warren.PipelineQueuesFactory;
import org.springframework.stereotype.Component;

@Component
public class CsvFileReaderTaskFactory {

    private Config config;
    private PipelineQueuesFactory pipelineQueuesFactory;

    public CsvFileReaderTaskFactory(Config config, PipelineQueuesFactory pipelineQueuesFactory) {
        this.config = config;
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public CsvFileReaderTask newTask() {
        return new CsvFileReaderTask(config, pipelineQueuesFactory.getOHLCVToBeProccessedQueue());
    }
}
