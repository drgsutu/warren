package io.sutu.DataProviders.Bittrex;

import com.google.gson.annotations.SerializedName;

public class BittrexResult {

    private final String success;

    private final String message;

    @SerializedName("result")
    private final BittrexTicker ticker;

    BittrexResult(String success, String message, BittrexTicker ticker) {
        this.success = success;
        this.message = message;
        this.ticker = ticker;
    }

    public BittrexTicker getTicker() {
        return ticker;
    }
}
