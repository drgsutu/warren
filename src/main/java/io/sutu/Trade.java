package io.sutu;

public class Trade {

    private final String fromCurrency;
    private final String toCurrency;
    private final double price;
    private final long timeStamp;
    private final double volume;

    public Trade(
            String fromCurrency,
            String toCurrency,
            double price,
            long timeStamp,
            double volume
    ) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.price = price;
        this.timeStamp = timeStamp;
        this.volume = volume;
    }

    public String getMarket() {
        return fromCurrency + '-' + toCurrency;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getPrice() {
        return price;
    }

    public double getVolume() {
        return volume;
    }
}
