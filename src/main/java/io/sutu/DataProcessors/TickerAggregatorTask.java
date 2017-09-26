package io.sutu.DataProcessors;

import io.sutu.DataProviders.CryptoCompare.MarketData;

import java.time.Instant;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TickerAggregatorTask implements Runnable {

    private Deque<MarketData> rawDataStorage;
    private Deque<OHLC> ohlcStorage;

    public TickerAggregatorTask(Deque<MarketData> rawDataStorage, Deque<OHLC> ohlcStorage) {
        this.rawDataStorage = rawDataStorage;
        this.ohlcStorage = ohlcStorage;
    }

    private static Predicate<MarketData> isNewerThanMoment(long startMoment) {
        return item -> item.getLastUpdate() > startMoment;
    }

    @Override
    public void run() {
        long now = Instant.now().getEpochSecond();
        int interval = 300;
        long startMoment = now - interval;

        double high = 0;
        double low = 0;
        List<MarketData> l = rawDataStorage.stream()
                .filter(isNewerThanMoment(startMoment))
                .collect(Collectors.toList());

        double open = l.get(l.size() - 1).getPrice();
        double close = l.get(0).getPrice();
        for (MarketData marketData : l) {
            double price = marketData.getPrice();
            if (price > high) {
                high = price;
            }

            if (price < low || 0 == low) {
                low = price;
            }
        }

        OHLC ohlc = new OHLC(open, high, low, close);
        ohlcStorage.add(ohlc);

        System.out.println(String.format("[%s] O: %.8f | H: %.8f | L: %.8f | C: %.8f", new Date(), open, high, low, close));
    }

}
