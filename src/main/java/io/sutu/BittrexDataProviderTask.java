package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClientException;

public class BittrexDataProviderTask implements Runnable {

    private final String market;

    private final BittrexClient bittrexClient;

    private final Storage storage;

    BittrexDataProviderTask(String market, BittrexClient bittrexClient, Storage storage) {
        this.market = market;
        this.bittrexClient = bittrexClient;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            // get data
            String ticker = bittrexClient.getTicker(market);

            // store data
            storage.add(ticker);
            // calculate indicators
            // buy/sell
        } catch (BittrexClientException e) {
            e.printStackTrace();
        }
    }
}
