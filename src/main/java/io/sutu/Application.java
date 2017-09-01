package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;

import java.util.Timer;

class Application {

    BittrexClient bittrexClient;

    Application(BittrexClient bittrexClient) {
        this.bittrexClient = bittrexClient;
    }

    void run() {
        Timer timer = new Timer();
        BittrexDataProviderTask task = new BittrexDataProviderTask();
        timer.scheduleAtFixedRate(task, 0, 10 * 1000);
    }
}
