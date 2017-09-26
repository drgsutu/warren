package io.sutu.DataProcessors;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.RetCode;

import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class IndicatorCalculationTask implements Runnable {

    private Deque<OHLC> ohlcStorage;
    private Core core;

    public IndicatorCalculationTask(Deque<OHLC> ohlcStorage, Core core) {
        this.ohlcStorage = ohlcStorage;
        this.core = core;
    }

    @Override
    public void run() {
        List<Double> l = ohlcStorage.stream()
                .limit(14)
                .map(item -> item.getClose())
                .collect(Collectors.toList());

//        RetCode retCode = core.rsi();

    }
}
