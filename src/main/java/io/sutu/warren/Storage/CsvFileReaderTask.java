package io.sutu.warren.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CsvFileReaderTask implements Runnable {

    private BlockingQueue<List<String>> OHLCVQueue;

    CsvFileReaderTask(BlockingQueue<List<String>> OHLCVQueue) {
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        String filePath = "ohlcv.csv";

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath))) {

            while (!Thread.interrupted()) {
                String line = bufferedReader.readLine();
                List<String> ohlcv = Arrays.asList(line.split(","));

                try {
                    OHLCVQueue.put(ohlcv);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
