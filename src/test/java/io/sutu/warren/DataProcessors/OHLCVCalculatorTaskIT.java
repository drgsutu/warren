package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.Tick;
import io.sutu.warren.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OHLCVCalculatorTaskIT {

    private OHLCVCalculatorTask sut;
    private BlockingQueue<Trade> rawDataQueue;
    private BlockingQueue<Tick> aggregatedDataQueue;

    private static final double PRICE = 0.00748021;
    private static final double[] VOLUMES = { 0.04181078, 0.19647575, 0.00500374, 5.20110000 };

    @BeforeEach
    void init() {
        rawDataQueue = new LinkedBlockingQueue<>();
        aggregatedDataQueue = new LinkedBlockingQueue<>();
        int interval = 60;

        sut = new OHLCVCalculatorTask(rawDataQueue, aggregatedDataQueue, interval);
    }

    @Test
    void taskCreatesTickOnlyWhenATradeWithNextTickIntervalIsAggregatedTest() {
        // given
        Trade[] trades = {
                new Trade("NEO", "BTC", PRICE, 1507221469, VOLUMES[0]),
                new Trade("NEO", "BTC", PRICE, 1507221473, VOLUMES[1]),
                new Trade("NEO", "BTC", PRICE, 1507221477, VOLUMES[2]),
                new Trade("NEO", "BTC", PRICE, 1507221479, VOLUMES[3]),
                // next Tick interval trade
                new Trade("NEO", "BTC", 0.00748023, 1507221488, 0.00300374)
        };

        try {
            for (Trade trade : trades) {
                rawDataQueue.put(trade);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(sut);

        Tick tick = null;
        try {
            tick = aggregatedDataQueue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();

        // then
        assertNotNull(tick, "Task did not create Tick");
        // validate the tick by it's OHLCV values
        assertEquals(PRICE, tick.getOpenPrice().toDouble(), "Open price does not match");
        assertEquals(PRICE, tick.getMaxPrice().toDouble(), "High price does not match");
        assertEquals(PRICE, tick.getMinPrice().toDouble(), "Low price does not match");
        assertEquals(PRICE, tick.getClosePrice().toDouble(), "Close price does not match");
        assertEquals(Arrays.stream(VOLUMES).sum(), tick.getVolume().toDouble(), "Volume does not match");
    }
}
