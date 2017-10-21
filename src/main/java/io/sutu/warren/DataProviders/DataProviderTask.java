package io.sutu.warren.DataProviders;

import io.sutu.warren.Communication.Kraken.HttpClient;
import io.sutu.warren.Communication.Kraken.HttpClientException;
import io.sutu.warren.Communication.Kraken.HttpResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class DataProviderTask implements Runnable {

    final static Logger logger = LoggerFactory.getLogger(DataProviderTask.class);

    private String market;
    private int intervalSeconds;
    private HttpClient httpClient;
    private List<BlockingQueue<List<String>>> OHLCVQueues;

    DataProviderTask(
            String market,
            int intervalSeconds,
            HttpClient httpClient,
            List<BlockingQueue<List<String>>> OHLCVQueues
    ) {
        this.market = market;
        this.intervalSeconds = intervalSeconds;
        this.httpClient = httpClient;
        this.OHLCVQueues = OHLCVQueues;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            final Long now = Instant.now().getEpochSecond();
            final Long since = now - (intervalSeconds * 2) - 1;
            final int intervalMinutes = intervalSeconds / 60;

            HttpResponseResult httpResponseResult;
            try {
                httpResponseResult = httpClient.getOHCLVData(market, intervalMinutes, since);
            } catch (HttpClientException e) {
                logger.error(e.getMessage(), e);
                continue;
            }

            Optional<List<String>> ohlcv = httpResponseResult.getPair()
                    .stream()
                    // remove invalid API responses
                    .filter(arg -> Long.parseLong(arg.get(0)) < 8_000_000_000L)
                    // get the last complete OHLCV using the timestamp provided by the API
                    .filter(arg -> arg.get(0).equals(httpResponseResult.getLast()))
                    .findFirst();

            if (!ohlcv.isPresent()) {
                continue;
            }

            try {
                for (BlockingQueue<List<String>> queue : OHLCVQueues) {
                    queue.put(ohlcv.get());
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.warn("Thread interrupted");
            }
        }
    }
}
