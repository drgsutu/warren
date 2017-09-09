package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Application {

    public static final int REQUESTS_PERIOD_SECONDS = 10;

    private BittrexClient bittrexClient;

    private Storage storage;

    Application(BittrexClient bittrexClient, Storage storage) {
        this.bittrexClient = bittrexClient;
        this.storage = storage;
    }

    void run() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(cpuCores);

        BittrexDataProviderTask bittrexDataProviderTask = new BittrexDataProviderTask(bittrexClient, storage);
        executorService.scheduleAtFixedRate(bittrexDataProviderTask, 0, REQUESTS_PERIOD_SECONDS, TimeUnit.SECONDS);
    }
}
