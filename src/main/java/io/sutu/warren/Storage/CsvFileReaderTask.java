package io.sutu.warren.Storage;

import io.sutu.warren.Trade;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

public class CsvFileReaderTask implements Runnable {

    private BlockingQueue<Trade> tradesQueue;

    CsvFileReaderTask(BlockingQueue<Trade> tradesQueue) {
        this.tradesQueue = tradesQueue;
    }

    @Override
    public void run() {
        String filePath = "trades.csv";
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath))) {
            while (!Thread.interrupted()) {
                String line = bufferedReader.readLine();
                String[] columns = line.split(",");
                String[] market = columns[0].split("-");
                Trade trade = new Trade(
                        market[0],
                        market[1],
                        Double.parseDouble(columns[1]),
                        Long.parseLong(columns[2]),
                        Double.parseDouble(columns[3])
                );

                try {
                    tradesQueue.put(trade);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
