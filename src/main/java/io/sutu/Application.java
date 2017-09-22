package io.sutu;

import io.sutu.DataProcessors.TickerAggregatorTask;
import io.sutu.DataProcessors.TickerAggregatorTaskFactory;
import io.sutu.DataProviders.CryptoCompare.SocketClient;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
class Application {

    private SocketClient socketClient;
    private TickerAggregatorTaskFactory tickerAggregatorTaskFactory;

    public Application(SocketClient socketClient, TickerAggregatorTaskFactory tickerAggregatorTaskFactory) {
        this.socketClient = socketClient;
        this.tickerAggregatorTaskFactory = tickerAggregatorTaskFactory;
    }

    void run() {
        String[] markets = {
            "ETHBTC"
        };

        socketClient.subscribe(markets);

        int cpuCores = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(cpuCores);

        long now = Instant.now().getEpochSecond();
        short delay = (short)((now % 300) + 300);
        int period = 300;
        for (String market : markets) {
            TickerAggregatorTask tickerAggregatorTask = tickerAggregatorTaskFactory.newTaskForMarket(market);
            executorService.scheduleAtFixedRate(tickerAggregatorTask, delay, period, TimeUnit.SECONDS);
        }

        // apply indicators on reformatted data

        // buy / sell
    }
}
