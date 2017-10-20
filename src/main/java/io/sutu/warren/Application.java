package io.sutu.warren;

import com.typesafe.config.Config;
import io.sutu.warren.DataProcessors.OHLCVCalculatorTask;
import io.sutu.warren.DataProcessors.OHLCVCalculatorTaskFactory;
import io.sutu.warren.DataProcessors.IndicatorCalculatorTask;
import io.sutu.warren.DataProcessors.IndicatorCalculatorTaskFactory;
import io.sutu.warren.Communication.CryptoCompare.SocketClient;
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
            "BTC-USD"
//            "NEO-BTC"
//            "XLM-BTC"
//            "XRP-BTC"
    };
    private static final int OHLCV_INTERVAL_SECONDS = 60;

    private Config config;
    private SocketClient socketClient;
    private CsvFileReaderTaskFactory csvFileReaderTaskFactory;
    private OHLCVCalculatorTaskFactory OHLCVCalculatorTaskFactory;
    private CsvFileWriterTaskFactory csvFileWriterTaskFactory;
    private IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory;

    public Application(
            Config config,
            SocketClient socketClient,
            CsvFileReaderTaskFactory csvFileReaderTaskFactory,
            OHLCVCalculatorTaskFactory OHLCVCalculatorTaskFactory,
            CsvFileWriterTaskFactory csvFileWriterTaskFactory,
            IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory
    ) {
        this.config = config;
        this.socketClient = socketClient;
        this.csvFileReaderTaskFactory = csvFileReaderTaskFactory;
        this.OHLCVCalculatorTaskFactory = OHLCVCalculatorTaskFactory;
        this.csvFileWriterTaskFactory = csvFileWriterTaskFactory;
        this.indicatorCalculatorTaskFactory = indicatorCalculatorTaskFactory;
    }

    void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // get trades data
        if (config.getBoolean("liveDataSource")) {
            // live data -> trades
            socketClient.subscribe(MARKETS);
        } else {
            // file data -> trades
            CsvFileReaderTask csvFileReaderTask = csvFileReaderTaskFactory.newTask();
            executorService.submit(csvFileReaderTask);
        }

        // trades -> OHLCV // aggregate trades data into OHLCV periods
        OHLCVCalculatorTask OHLCVCalculatorTask = OHLCVCalculatorTaskFactory.newTaskForInterval(OHLCV_INTERVAL_SECONDS);
        executorService.submit(OHLCVCalculatorTask);

        if (config.getBoolean("writeTradesToFile")) {
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
