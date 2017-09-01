package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;

class Warren {
    public static void main(String[] args) {
        Application app = new Application(new BittrexClient());
        app.run();
    }
}
