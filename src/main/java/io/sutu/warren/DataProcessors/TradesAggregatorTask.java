package io.sutu.warren.DataProcessors;

import eu.verdelhan.ta4j.BaseTick;
import eu.verdelhan.ta4j.Tick;
import io.sutu.warren.Trade;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.BlockingQueue;

public class TradesAggregatorTask implements Runnable {

    private final BlockingQueue<Trade> rawDataQueue;
    private final BlockingQueue<Tick> aggregatedDataQueue;
    private int interval;

    TradesAggregatorTask(BlockingQueue<Trade> rawDataQueue, BlockingQueue<Tick> aggregatedDataQueue, int interval) {
        this.rawDataQueue = rawDataQueue;
        this.aggregatedDataQueue = aggregatedDataQueue;
        this.interval = interval;
    }

    @Override
    public void run() {
        // TODO: first tick is incomplete so it should not be put in the queue
        Duration tickTimePeriod = Duration.ofSeconds(interval);
        long currentTickEndTimeStamp = 0;
        Tick tick = null;

        while (!Thread.interrupted()) {
            try {
                Trade trade = rawDataQueue.take();

                long tradeTimeStamp = trade.getTimeStamp();
                long targetTickEndTimeStamp = tradeTimeStamp - (tradeTimeStamp % interval) + interval;

                // The Tick that we are building is identified by it's end timestamp
                // From each trade we calculate the Tick it should belong to by the trade's timestamp
                // If the current Tick's timestamp is different that the trade's target Tick's timestamp,
                // it means this trade is the first trade from the next Tick
                // If trades are not ordered by date we are screwed!
                if (currentTickEndTimeStamp != targetTickEndTimeStamp) {
                    // pass the current tick to the next element in the pipeline, by putting it in the next queue
                    if (null != tick) {
                        aggregatedDataQueue.put(tick);
                    }

                    ZonedDateTime tickEndTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(targetTickEndTimeStamp), ZoneId.of("UTC"));
//                    ZonedDateTime tradeTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(tradeTimeStamp), ZoneId.of("UTC"));
//                    System.out.println(tradeTime);
//                    System.out.println(tickEndTime);
                    // replace the old tick with the new one
                    tick = new BaseTick(tickTimePeriod, tickEndTime);
                    // update timestamp identifier
                    currentTickEndTimeStamp = targetTickEndTimeStamp;
                }

                tick.addTrade(trade.getVolume(), trade.getPrice());

            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }
        }
    }
}
