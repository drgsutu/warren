package io.sutu.warren;

import com.typesafe.config.Config;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PipelineQueuesFactory {

    private BlockingQueue<List<String>> OHLCVToBeProccessedQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<List<String>> OHLCVToBeStoredQueue =  new LinkedBlockingQueue<>();
    private List<BlockingQueue<List<String>>> OHLCVQueues = new ArrayList<>();

    public PipelineQueuesFactory(Config config) {
        OHLCVQueues.add(OHLCVToBeProccessedQueue);
        if (config.getBoolean("data.writeToFile")) {
            OHLCVQueues.add(OHLCVToBeStoredQueue);
        }
    }

    public BlockingQueue<List<String>> getOHLCVToBeProccessedQueue() {
        return OHLCVToBeProccessedQueue;
    }

    public BlockingQueue<List<String>> getOHLCVToBeStoredQueue() {
        return OHLCVToBeStoredQueue;
    }

    public List<BlockingQueue<List<String>>> getOHLCVQueues() {
        return OHLCVQueues;
    }
}
