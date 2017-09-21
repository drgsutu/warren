package io.sutu.DataProviders.CryptoCompare;

public class MarketData {

    private final String fromCurrency;
    private final String toCurrency;
    private final double price;
    private final long lastUpdate;
    private final double lastVolume;
    private final double lastVolumeTo;

    public MarketData(
            String fromCurrency,
            String toCurrency,
            double price,
            long lastUpdate,
            double lastVolume,
            double lastVolumeTo
    ) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.price = price;
        this.lastUpdate = lastUpdate;
        this.lastVolume = lastVolume;
        this.lastVolumeTo = lastVolumeTo;
    }

    public String getMarket() {
        return fromCurrency + toCurrency;
    }

}
