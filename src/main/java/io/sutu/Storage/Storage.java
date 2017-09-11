package io.sutu.Storage;

import java.util.List;

public interface Storage {

    void add(Ticker ticker);

    List<Ticker> getTickersByTimeStamp(long timeStamp);
}
