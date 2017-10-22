package io.sutu.warren.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TradingTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TradingTask.class);

    private StrategiesService strategiesService;
    private BlockingQueue<List<String>> OHLCVQueue;

    TradingTask(
            StrategiesService strategiesService,
            BlockingQueue<List<String>> OHLCVQueue
    ) {
        this.strategiesService = strategiesService;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                List<String> ohlcv = OHLCVQueue.take();
                strategiesService.addOHLCV(ohlcv);

                // do not calculate until we have the minimum historical data to calculate indicators
                if (strategiesService.isReadyForTrading()) {
                    continue;
                }

                if (strategiesService.shouldEnter()) {
                    logger.info("Buy");
                }
                if (strategiesService.shouldExit()) {
                    logger.info("Sell");
                }

            } catch (InterruptedException e) {
                logger.warn("Thread interrupted", e);
            }
        }
    }
}
