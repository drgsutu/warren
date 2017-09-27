package io.sutu.DataProcessors;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.util.Date;
import java.util.Deque;

public class IndicatorCalculationTask implements Runnable {

    private Deque<OHLC> ohlcStorage;
    private Core core;

    public IndicatorCalculationTask(Deque<OHLC> ohlcStorage, Core core) {
        this.ohlcStorage = ohlcStorage;
        this.core = core;
    }

    @Override
    public void run() {
        double[] l = ohlcStorage.stream()
                .limit(14)
                .mapToDouble(OHLC::getClose)
                .toArray();

        int startIndex = 0;
        int endIndex = 0;
        int periodsLength = 14;
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double[] result = {};

        RetCode retCode = core.rsi(startIndex, endIndex, l, periodsLength, begin, length, result);
        System.out.println(String.format("[%s] START RSI", new Date()));
        for (int i = 0; i < l.length; i++) {
            System.out.println(l[i]);
        }
        System.out.println(retCode);
        System.out.println(begin.value);
        System.out.println(length.value);
        System.out.println(result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
        System.out.println(String.format("[%s] END RSI", new Date()));
    }
}
