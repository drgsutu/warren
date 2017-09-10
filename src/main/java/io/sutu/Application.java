package io.sutu;

import io.sutu.DataProviders.Bittrex.BittrexDataProviderTask;
import io.sutu.DataProviders.Bittrex.BittrexDataProviderTaskFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
class Application {

    static final int REQUESTS_PERIOD_SECONDS = 10;

    private BittrexDataProviderTaskFactory bittrexDataProviderTaskFactory;

    public Application(BittrexDataProviderTaskFactory bittrexDataProviderTaskFactory) {
        this.bittrexDataProviderTaskFactory = bittrexDataProviderTaskFactory;
    }

    void run() {
        String[] markets = {"BTC-ETH", "BTC-NEO", "BTC-OMG"};

        int cpuCores = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(cpuCores);

        for (String market : markets) {
            BittrexDataProviderTask bittrexDataProviderTask = bittrexDataProviderTaskFactory.newTaskForMarket(market);
            executorService.scheduleAtFixedRate(bittrexDataProviderTask, 0, REQUESTS_PERIOD_SECONDS, TimeUnit.SECONDS);
        }
    }
}
