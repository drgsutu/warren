package io.sutu.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BtcEthStorage implements Storage {

    private ArrayList<Ticker> storage = new ArrayList<>();

    public void add(Ticker ticker) {
        storage.add(ticker);
    }

    public List<Ticker> getTickersByTimeStamp(long timeStamp) {
        System.out.println(storage);
        return storage.stream()
                .filter(item -> item.getTimeStamp() > timeStamp)
                .collect(Collectors.toList());
    }
}
