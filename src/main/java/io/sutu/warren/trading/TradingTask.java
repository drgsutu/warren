package io.sutu.warren.trading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TradingTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TradingTask.class);

    private TradingStrategiesService tradingStrategiesService;
    private BlockingQueue<List<String>> OHLCVQueue;

    TradingTask(
            TradingStrategiesService tradingStrategiesService,
            BlockingQueue<List<String>> OHLCVQueue
    ) {
        this.tradingStrategiesService = tradingStrategiesService;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                List<String> ohlcv = OHLCVQueue.take();
                tradingStrategiesService.addOHLCV(ohlcv);

                // do not calculate until we have the minimum historical data to calculate indicators
                if (!tradingStrategiesService.isReadyForTrading()) {
                    continue;
                }

                if (tradingStrategiesService.shouldEnter()) {
                    logger.info("Buy");
                }
                if (tradingStrategiesService.shouldExit()) {
                    logger.info("Sell");
                }

            } catch (InterruptedException e) {
                logger.warn("Thread interrupted", e);
            }
        }
    }
}
