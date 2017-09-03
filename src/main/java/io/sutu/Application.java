package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Application {

    BittrexClient bittrexClient;

    Application(BittrexClient bittrexClient) {
        this.bittrexClient = bittrexClient;
    }

    void run() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(cpuCores);
        BittrexDataProviderTask bittrexDataProviderTask = new BittrexDataProviderTask();
        scheduledExecutorService.scheduleAtFixedRate(bittrexDataProviderTask, 0, 10, TimeUnit.SECONDS);
    }
}
