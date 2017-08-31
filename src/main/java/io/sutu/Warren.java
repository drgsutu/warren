package io.sutu;

import io.sutu.Communications.Bittrex.BittrexClient;

class Warren {
    public static void main(String[] args) {
        Application app = new Application(new BittrexClient());
        app.run();
    }
}
