package io.sutu.warren;

import com.typesafe.config.Config;
import io.sutu.warren.DataProcessors.IndicatorCalculatorTask;
import io.sutu.warren.DataProcessors.IndicatorCalculatorTaskFactory;
import io.sutu.warren.DataProviders.DataProviderTask;
import io.sutu.warren.DataProviders.DataProviderTaskFactory;
import io.sutu.warren.Storage.CsvFileReaderTask;
import io.sutu.warren.Storage.CsvFileReaderTaskFactory;
import io.sutu.warren.Storage.CsvFileWriterTask;
import io.sutu.warren.Storage.CsvFileWriterTaskFactory;
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
    private IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory;

    public Application(
            Config config,
            DataProviderTaskFactory dataProviderTaskFactory,
            CsvFileReaderTaskFactory csvFileReaderTaskFactory,
            CsvFileWriterTaskFactory csvFileWriterTaskFactory,
            IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory
    ) {
        this.config = config;
        this.dataProviderTaskFactory = dataProviderTaskFactory;
        this.csvFileReaderTaskFactory = csvFileReaderTaskFactory;
        this.csvFileWriterTaskFactory = csvFileWriterTaskFactory;
        this.indicatorCalculatorTaskFactory = indicatorCalculatorTaskFactory;
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
        IndicatorCalculatorTask indicatorCalculatorTask = indicatorCalculatorTaskFactory.newTaskForMarket(MARKETS[0]);
        executorService.submit(indicatorCalculatorTask);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdownNow();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }));
    }
}
