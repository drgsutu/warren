package io.sutu.warren;

import io.sutu.warren.DataProcessors.TradesAggregatorTask;
import io.sutu.warren.DataProcessors.TradesAggregatorTaskFactory;
import io.sutu.warren.DataProcessors.IndicatorCalculatorTask;
import io.sutu.warren.DataProcessors.IndicatorCalculatorTaskFactory;
import io.sutu.warren.Communication.CryptoCompare.SocketClient;
import io.sutu.warren.Storage.CsvFileWriterTask;
import io.sutu.warren.Storage.CsvFileWriterTaskFactory;
import io.sutu.warren.Trading.TradingTask;
import io.sutu.warren.Trading.TradingTaskFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
class Application {

    static final String[] MARKETS = {
            "NEO-BTC"
    };

    private SocketClient socketClient;
    private TradesAggregatorTaskFactory tradesAggregatorTaskFactory;
    private CsvFileWriterTaskFactory csvFileWriterTaskFactory;
    private IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory;
    private TradingTaskFactory tradingTaskFactory;

    public Application(
            SocketClient socketClient,
            TradesAggregatorTaskFactory tradesAggregatorTaskFactory,
            CsvFileWriterTaskFactory csvFileWriterTaskFactory,
            IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory,
            TradingTaskFactory tradingTaskFactory
    ) {
        this.socketClient = socketClient;
        this.tradesAggregatorTaskFactory = tradesAggregatorTaskFactory;
        this.csvFileWriterTaskFactory = csvFileWriterTaskFactory;
        this.indicatorCalculatorTaskFactory = indicatorCalculatorTaskFactory;
        this.tradingTaskFactory = tradingTaskFactory;
    }

    void run() {
        // get the data
        socketClient.subscribe(MARKETS);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // aggregate the data into OHLCV ticks
        TradesAggregatorTask tradesAggregatorTask = tradesAggregatorTaskFactory.newTask();
        executorService.submit(tradesAggregatorTask);

        // save data to file
        CsvFileWriterTask csvFileWriterTask = csvFileWriterTaskFactory.newTask();
        executorService.submit(csvFileWriterTask);

        // calculate indicators
        IndicatorCalculatorTask indicatorCalculatorTask = indicatorCalculatorTaskFactory.newTask();
        executorService.submit(indicatorCalculatorTask);

        // take trading decisions
        TradingTask tradingTask = tradingTaskFactory.newTask();
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
