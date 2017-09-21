package io.sutu;

import io.sutu.DataProviders.CryptoCompare.SocketClient;
import org.springframework.stereotype.Component;

@Component
class Application {

    private SocketClient socketClient;

    public Application(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    void run() {
        String[] markets = {
            "ETHBTC"
        };

        socketClient.subscribe(markets);

        // apply indicators on reformatted data

        // buy / sell
    }
}
