package io.sutu;

import io.sutu.DataProviders.Bittrex.Communications.BittrexClient;
import io.sutu.DataProviders.Bittrex.Communications.BittrexClientException;

import java.util.Date;
import java.util.TimerTask;

public class BittrexDataProviderTask extends TimerTask {

    @Override
    public void run() {
        try {
            BittrexClient bittrexClient = new BittrexClient();
            String ticker = bittrexClient.getTicker("BTC-ETH");
            Date date = new Date();
            System.out.println(String.format("[%s] %s", date, ticker));
        } catch (BittrexClientException e) {
            e.printStackTrace();
        }
    }
}