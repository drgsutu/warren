package io.sutu.warren.Storage;

import io.sutu.warren.Trade;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;

public class CsvFileWriterTask implements Runnable {

    private final BlockingQueue<Trade> rawDataToBeStoredQueue;

    CsvFileWriterTask(BlockingQueue<Trade> rawDataToBeStoredQueue) {
        this.rawDataToBeStoredQueue = rawDataToBeStoredQueue;
    }

    @Override
    public void run() {
        String path = "trades2.csv";
        try (
                BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                PrintWriter writer = new PrintWriter(bufferedWriter)
        ) {
            try {
                while (!Thread.interrupted()) {
                    Trade trade = rawDataToBeStoredQueue.take();
                    String csvLine = String.join(
                            ",",
                            trade.getMarket(),
                            Double.toString(trade.getPrice()),
                            Long.toString(trade.getTimeStamp()),
                            Double.toString(trade.getVolume())
                    );
                    writer.println(csvLine);
                    System.out.println("Wrote to buffer: " + csvLine);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
