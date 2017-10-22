package io.sutu.warren.communication.kraken;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HttpResponseResult {

    @SerializedName(value = "XXBTZEUR", alternate = {"XETHZEUR"})
    private List<List<String>> pair;
    private String last;

    public void setPair(List<List<String>> pair) {
        this.pair = pair;
    }

    public List<List<String>> getPair() {
        return pair;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLast() {
        return last;
    }
}
