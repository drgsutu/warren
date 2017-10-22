package io.sutu.warren;

import com.typesafe.config.Config;
import io.sutu.warren.trading.TradingTask;
import io.sutu.warren.trading.TradingTaskFactory;
import io.sutu.warren.dataProviders.DataProviderTask;
import io.sutu.warren.dataProviders.DataProviderTaskFactory;
import io.sutu.warren.storage.CsvFileReaderTask;
import io.sutu.warren.storage.CsvFileReaderTaskFactory;
import io.sutu.warren.storage.CsvFileWriterTask;
import io.sutu.warren.storage.CsvFileWriterTaskFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
class Application {

    private static final String[] MARKETS = {
            "XBTEUR",
//            "BTC-USD"
//            "NEO-BTC"
//            "XLM-BTC"
//            "XRP-BTC"
    };
    private static final int OHLCV_INTERVAL_SECONDS = 300;

    private Config config;
    private DataProviderTaskFactory dataProviderTaskFactory;
    private CsvFileReaderTaskFactory csvFileReaderTaskFactory;
    private CsvFileWriterTaskFactory csvFileWriterTaskFactory;
    private TradingTaskFactory tradingTaskFactory;

    public Application(
            Config config,
            DataProviderTaskFactory dataProviderTaskFactory,
            CsvFileReaderTaskFactory csvFileReaderTaskFactory,
            CsvFileWriterTaskFactory csvFileWriterTaskFactory,
            TradingTaskFactory tradingTaskFactory
    ) {
        this.config = config;
        this.dataProviderTaskFactory = dataProviderTaskFactory;
        this.csvFileReaderTaskFactory = csvFileReaderTaskFactory;
        this.csvFileWriterTaskFactory = csvFileWriterTaskFactory;
        this.tradingTaskFactory = tradingTaskFactory;
    }

    void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // get trades data
        if (config.getBoolean("data.source.live")) {
            // live data -> OHLCV
            DataProviderTask dataProviderTask =
                    dataProviderTaskFactory.newTaskForMarketAndInterval(MARKETS[0], OHLCV_INTERVAL_SECONDS);
            executorService.submit(dataProviderTask);
        } else {
            // file data -> OHLCV
            CsvFileReaderTask csvFileReaderTask = csvFileReaderTaskFactory.newTask();
            executorService.submit(csvFileReaderTask);
        }

        if (config.getBoolean("data.writeToFile")) {
            // trades -> file // save trades to file
            CsvFileWriterTask csvFileWriterTask = csvFileWriterTaskFactory.newTask();
            executorService.submit(csvFileWriterTask);
        }

        // OHLCV -> indicators // calculate indicators from OHLCV periods
        TradingTask tradingTask = tradingTaskFactory.newTaskForMarket();
        executorService.submit(tradingTask);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdownNow();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }));
    }
}
