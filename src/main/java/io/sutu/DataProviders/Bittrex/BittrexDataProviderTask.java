package io.sutu.DataProviders.Bittrex;

import com.google.gson.Gson;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClientException;
import io.sutu.Storage.Storage;
import io.sutu.Storage.Ticker;

import java.time.Instant;
import java.util.Date;

public class BittrexDataProviderTask implements Runnable {

    private final BittrexClient bittrexClient;

    private Gson gson;

    private Storage storage;

    private String market;

    BittrexDataProviderTask(BittrexClient bittrexClient, Gson gson) {
        this.bittrexClient = bittrexClient;
        this.gson = gson;
    }

    BittrexDataProviderTask setStorage(Storage storage) {
        this.storage = storage;
        return this;
    }

    BittrexDataProviderTask setMarket(String market) {
        this.market = market;
        return this;
    }

    @Override
    public void run() {
        try {
            // get data
            Instant instant = Instant.now();
            long timeStamp = instant.getEpochSecond();
            String resultRaw = bittrexClient.getTicker(market);

            // deserialize data
            BittrexResult result = gson.fromJson(resultRaw, BittrexResult.class);

            System.out.println(String.format("[%s][%s] %s %s", Thread.currentThread().getName(), new Date(), market, result.getTicker().getLast()));
            // store data
            storage.add(new Ticker(timeStamp, result.getTicker().getLast()));

        } catch (BittrexClientException e) {
            e.printStackTrace();
        }
    }
}
