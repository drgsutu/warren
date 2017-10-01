package io.sutu;

import io.sutu.DataProcessors.DataAggregatorTask;
import io.sutu.DataProcessors.DataAggregatorTaskFactory;
import io.sutu.DataProcessors.IndicatorCalculatorTask;
import io.sutu.DataProcessors.IndicatorCalculatorTaskFactory;
import io.sutu.DataProviders.CryptoCompare.SocketClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
class Application {

    private SocketClient socketClient;
    private DataAggregatorTaskFactory dataAggregatorTaskFactory;
    private IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory;

    public Application(
            SocketClient socketClient,
            DataAggregatorTaskFactory dataAggregatorTaskFactory,
            IndicatorCalculatorTaskFactory indicatorCalculatorTaskFactory
    ) {
        this.socketClient = socketClient;
        this.dataAggregatorTaskFactory = dataAggregatorTaskFactory;
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
        executorService.execute(dataAggregatorTask);

        // calculate indicators
        IndicatorCalculatorTask indicatorCalculatorTask = indicatorCalculatorTaskFactory.newTask();
        executorService.execute(indicatorCalculatorTask);
    }
}
