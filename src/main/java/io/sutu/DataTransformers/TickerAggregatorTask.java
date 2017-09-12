package io.sutu.DataTransformers;

import io.sutu.Storage.Storage;
import io.sutu.Storage.Ticker;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class TickerAggregatorTask implements Runnable {

    private volatile Storage storage;

    public TickerAggregatorTask(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        Instant instant = Instant.now();
        long timeStamp = instant.getEpochSecond() - 60;
        List<Ticker> tickers = storage.getTickersByTimeStamp(timeStamp);

        System.out.println(String.format("[%s][%s] %s", Thread.currentThread().getName(), new Date(), tickers.size()));
    }

}
