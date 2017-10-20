package io.sutu.warren;

import com.typesafe.config.Config;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class PipelineQueuesFactory {

    private BlockingQueue<Trade> tradesToBeAggregatedQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<Trade> tradesToBeStoredQueue =  new LinkedBlockingQueue<>();
    private BlockingQueue<List<String>> OHLCVQueue =  new LinkedBlockingQueue<>();
    private List<BlockingQueue<Trade>> tradesQueues = new ArrayList<>();

    public PipelineQueuesFactory(Config config) {
        tradesQueues.add(tradesToBeAggregatedQueue);
        if (config.getBoolean("writeTradesToFile")) {
            tradesQueues.add(tradesToBeStoredQueue);
        }
    }

    public BlockingQueue<Trade> getTradesToBeAggregatedQueue() {
        return tradesToBeAggregatedQueue;
    }

    public BlockingQueue<Trade> getTradesToBeStoredQueue() {
        return tradesToBeStoredQueue;
    }

    public List<BlockingQueue<Trade>> getTradesQueues() {
        return tradesQueues;
    }

    public BlockingQueue<List<String>> getOHLCVQueue() {
        return OHLCVQueue;
    }
}
