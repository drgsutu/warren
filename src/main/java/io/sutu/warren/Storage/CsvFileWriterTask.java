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

    private final BlockingQueue<Trade> tradesQueue;

    CsvFileWriterTask(BlockingQueue<Trade> tradesQueue) {
        this.tradesQueue = tradesQueue;
    }

    @Override
    public void run() {
        String path = "trades.csv";
        try (
                BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                PrintWriter writer = new PrintWriter(bufferedWriter)
        ) {
            try {
                while (!Thread.interrupted()) {
                    Trade trade = tradesQueue.take();
                    String csvLine = String.join(
                            ",",
                            trade.getMarket(),
                            Double.toString(trade.getPrice()),
                            Long.toString(trade.getTimeStamp()),
                            Double.toString(trade.getVolume())
                    );
                    writer.println(csvLine);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + getClass().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
