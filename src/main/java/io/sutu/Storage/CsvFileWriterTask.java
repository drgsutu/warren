package io.sutu.Storage;

import io.sutu.Trade;

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
        String path = "trades.csv";
        try (
                BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                PrintWriter writer = new PrintWriter(bufferedWriter)
        ) {
            while (true) {
                Trade trade = rawDataToBeStoredQueue.take();
                String csvLine = String.join(
                        ",",
                        trade.getMarket(),
                        Double.toString(trade.getPrice()),
                        Long.toString(trade.getTimeStamp()),
                        Double.toString(trade.getVolume())
                );
                writer.println(csvLine);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
