package io.sutu;

import io.sutu.Communications.Bittrex.BittrexClient;
import io.sutu.Communications.Bittrex.BittrexClientException;

class Application {

    BittrexClient bittrexClient;

    Application(BittrexClient bittrexClient) {
        this.bittrexClient = bittrexClient;
    }

    void run() {
        try {
            String ticker = bittrexClient.getTicker("BTC-ETH");
            System.out.println(ticker);
        } catch (BittrexClientException e) {
            e.printStackTrace();
        }
    }
}
