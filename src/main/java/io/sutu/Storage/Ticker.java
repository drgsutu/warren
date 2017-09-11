package io.sutu.Storage;

public class Ticker {

    private final long timeStamp;

    private final double price;

    public Ticker(long timeStamp, double price) {
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getPrice() {
        return price;
    }
}
