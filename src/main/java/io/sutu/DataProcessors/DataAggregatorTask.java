package io.sutu.DataProcessors;

import io.sutu.DataProviders.CryptoCompare.MarketData;

import java.time.Instant;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataAggregatorTask implements Runnable {

    private String market;
    private Deque<MarketData> rawDataStorage;
    private Deque<OHLC> ohlcStorage;
    private BlockingQueue<String> pipelineState;

    public DataAggregatorTask(
            String market,
            Deque<MarketData> rawDataStorage,
            Deque<OHLC> ohlcStorage,
            BlockingQueue pipelineState
    ) {
        this.market = market;
        this.rawDataStorage = rawDataStorage;
        this.ohlcStorage = ohlcStorage;
        this.pipelineState = pipelineState;
    }

    private static Predicate<MarketData> isNewerThanTimestamp(long startTimestamp) {
        return item -> item.getLastUpdate() > startTimestamp;
    }

    @Override
    public void run() {
        long now = Instant.now().getEpochSecond();
        int interval = 300;
        long startTimestamp = now - interval;

        List<MarketData> l = rawDataStorage.stream()
                .filter(isNewerThanTimestamp(startTimestamp))
                .collect(Collectors.toList());

        double open = l.get(l.size() - 1).getPrice();
        double close = l.get(0).getPrice();
        double high = 0;
        double low = 0;
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
        pipelineState.offer(market);

        System.out.println(String.format("[%s] O: %.8f | H: %.8f | L: %.8f | C: %.8f", new Date(), open, high, low, close));
    }

}
