package io.sutu.warren.DataProviders;

import io.sutu.warren.Communication.Kraken.HttpClient;
import io.sutu.warren.Communication.Kraken.HttpResponseResult;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class DataProviderTask implements Runnable {

    private String market;
    private int intervalSeconds;
    private HttpClient httpClient;
    private BlockingQueue<List<String>> OHLCVQueue;

    DataProviderTask(String market, int intervalSeconds,HttpClient httpClient, BlockingQueue<List<String>> OHLCVQueue) {
        this.market = market;
        this.intervalSeconds = intervalSeconds;
        this.httpClient = httpClient;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            final Long now = Instant.now().getEpochSecond();
            final Long since = now - (intervalSeconds * 2) - 1;
            final int intervalMinutes = intervalSeconds / 60;

            HttpResponseResult httpResponseResult = httpClient.getOHCLVData(market, intervalMinutes, since);

            Optional<List<String>> ohlcv = httpResponseResult.getPair()
                    .stream()
                    .peek(System.out::println)
                    .filter(arg -> arg.get(0).equals(httpResponseResult.getLast()) && Long.parseLong(arg.get(0)) < 8_000_000_000L)
                    .findFirst();

            try {
                OHLCVQueue.put(ohlcv.orElseThrow(Exception::new));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
