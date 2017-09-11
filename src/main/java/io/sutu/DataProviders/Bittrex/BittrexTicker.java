package io.sutu.DataProviders.Bittrex;

import com.google.gson.annotations.SerializedName;

public class BittrexTicker {

    @SerializedName("Bid")
    private final Double bid;

    @SerializedName("Ask")
    private final Double ask;

    @SerializedName("Last")
    private final Double last;

    public BittrexTicker(Double bid, Double ask, Double last) {
        this.bid = bid;
        this.ask = ask;
        this.last = last;
    }

    public Double getLast() {
        return last;
    }
}
