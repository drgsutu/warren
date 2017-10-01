package io.sutu;

import io.sutu.DataProcessors.DataAggregatorTask;
import io.sutu.DataProcessors.DataAggregatorTaskFactory;
import io.sutu.DataProcessors.IndicatorCalculatorTask;
import io.sutu.DataProcessors.IndicatorCalculatorTaskFactory;
import io.sutu.Communication.CryptoCompare.SocketClient;
import io.sutu.Storage.CsvFileWriterTask;
import io.sutu.Storage.CsvFileWriterTaskFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
class Application {

    private SocketClient socketClient;
    private DataAggregatorTaskFactory dataAggregatorTaskFactory;
    private CsvFileWriterTaskFactory csvFileWriterTaskFactory;
    private IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory;

    public Application(
            SocketClient socketClient,
            DataAggregatorTaskFactory dataAggregatorTaskFactory,
            CsvFileWriterTaskFactory csvFileWriterTaskFactory,
            IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory
    ) {
        this.socketClient = socketClient;
        this.dataAggregatorTaskFactory = dataAggregatorTaskFactory;
        this.csvFileWriterTaskFactory = csvFileWriterTaskFactory;
        this.indicatorCalculatorTaskFactory = indicatorCalculatorTaskFactory;
    }

    void run() {

        String[] markets = {
            "NEO-BTC"
//            "ETH-BTC"
        };
        // get the data
        socketClient.subscribe(markets);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // aggregate the data into OHLCV ticks
        DataAggregatorTask dataAggregatorTask = dataAggregatorTaskFactory.newTask();
        executorService.submit(dataAggregatorTask);

        // save data to file
        CsvFileWriterTask csvFileWriterTask = csvFileWriterTaskFactory.newTask();
        executorService.submit(csvFileWriterTask);

        // calculate indicators
        IndicatorCalculatorTask indicatorCalculatorTask = indicatorCalculatorTaskFactory.newTask();
        executorService.submit(indicatorCalculatorTask);

        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdownNow));
    }
}
