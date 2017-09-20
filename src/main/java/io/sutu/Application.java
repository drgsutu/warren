package io.sutu;

import io.sutu.DataProviders.Bittrex.BittrexDataProviderTask;
import io.sutu.DataProviders.Bittrex.BittrexDataProviderTaskFactory;
import io.sutu.DataProviders.Bittrex.BittrexMarkets;
import io.sutu.DataProcessors.TickerAggregatorTask;
import io.sutu.DataProcessors.TickerAggregatorTaskFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
class Application {

    static final int REQUESTS_PERIOD_SECONDS = 10;
    static final int CALCULATIONS_PERIOD_SECONDS = 60;

    private BittrexDataProviderTaskFactory bittrexDataProviderTaskFactory;
    private TickerAggregatorTaskFactory tickerAggregatorTaskFactory;

    public Application(
            BittrexDataProviderTaskFactory bittrexDataProviderTaskFactory,
            TickerAggregatorTaskFactory tickerAggregatorTaskFactory
        ) {
        this.bittrexDataProviderTaskFactory = bittrexDataProviderTaskFactory;
        this.tickerAggregatorTaskFactory = tickerAggregatorTaskFactory;
    }

    void run() {
        String[] markets = {
            BittrexMarkets.BTCETH,
            BittrexMarkets.BTCNEO,
            BittrexMarkets.BTCOMG,
        };

        int cpuCores = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(cpuCores);

        for (String market : markets) {
            // get data
            BittrexDataProviderTask bittrexDataProviderTask = bittrexDataProviderTaskFactory.newTaskForMarket(market);
            executorService.scheduleAtFixedRate(bittrexDataProviderTask, 0, REQUESTS_PERIOD_SECONDS, TimeUnit.SECONDS);

            // reformat data
            TickerAggregatorTask tickerAggregatorTask = tickerAggregatorTaskFactory.newTaskForMarket(market);
            executorService.scheduleAtFixedRate(tickerAggregatorTask, CALCULATIONS_PERIOD_SECONDS, CALCULATIONS_PERIOD_SECONDS, TimeUnit.SECONDS);
        }

        // apply indicators on reformatted data

        // buy / sell
    }
}
