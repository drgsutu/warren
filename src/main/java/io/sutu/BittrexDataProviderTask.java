package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClientException;

public class BittrexDataProviderTask implements Runnable {

    private final BittrexClient bittrexClient;
    private final Storage storage;

    BittrexDataProviderTask(BittrexClient bittrexClient, Storage storage) {
        this.bittrexClient = bittrexClient;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            // get data
            String ticker = bittrexClient.getTicker("BTC-ETH");

            // store data
            storage.add(ticker);
            // calculate indicators
            // buy/sell
        } catch (BittrexClientException e) {
            e.printStackTrace();
        }
    }
}
