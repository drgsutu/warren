package io.sutu.warren.storage;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CsvFileReaderTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CsvFileReaderTask.class);

    private Config config;
    private BlockingQueue<List<String>> OHLCVQueue;

    CsvFileReaderTask(Config config, BlockingQueue<List<String>> OHLCVQueue) {
        this.config = config;
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        String filePath = null;
        try {
            filePath = config.getString("data.source.filePath");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath))) {

            while (!Thread.interrupted()) {
                String line = bufferedReader.readLine();
                List<String> ohlcv = Arrays.asList(line.split(","));

                try {
                    OHLCVQueue.put(ohlcv);
                } catch (InterruptedException e) {
                    logger.warn("Thread interrupted", e);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
