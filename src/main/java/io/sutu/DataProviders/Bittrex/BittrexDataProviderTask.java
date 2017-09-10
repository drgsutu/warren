package io.sutu.DataProviders.Bittrex;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClientException;
import io.sutu.Storage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BittrexDataProviderTask implements Runnable {

    private final BittrexClient bittrexClient;

    private final Storage storage;

    private String market;

    BittrexDataProviderTask(BittrexClient bittrexClient, Storage storage) {
        this.bittrexClient = bittrexClient;
        this.storage = storage;
    }

    BittrexDataProviderTask setMarket(String market) {
        this.market = market;
        return this;
    }

    @Override
    public void run() {
        try {
            // get data
            String ticker = bittrexClient.getTicker(market);
            System.out.println(String.format("[%s] %s %s", new Date(), market, ticker));

            // store data
            storage.add(ticker);
            // calculate indicators
            // buy/sell
        } catch (BittrexClientException e) {
            e.printStackTrace();
        }
    }
}
