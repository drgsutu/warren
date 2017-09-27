package io.sutu;

import io.sutu.DataProcessors.DataAggregatorTask;
import io.sutu.DataProcessors.DataAggregatorTaskFactory;
import io.sutu.DataProcessors.IndicatorCalculationTask;
import io.sutu.DataProcessors.IndicatorCalculationTaskFactory;
import io.sutu.DataProviders.CryptoCompare.SocketClient;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.*;

@Component
class Application {

    private SocketClient socketClient;
    private DataAggregatorTaskFactory dataAggregatorTaskFactory;
    private IndicatorCalculationTaskFactory indicatorCalculationTaskFactory;

    public Application(
            SocketClient socketClient,
            DataAggregatorTaskFactory dataAggregatorTaskFactory,
            IndicatorCalculationTaskFactory indicatorCalculationTaskFactory
    ) {
        this.socketClient = socketClient;
        this.dataAggregatorTaskFactory = dataAggregatorTaskFactory;
        this.indicatorCalculationTaskFactory = indicatorCalculationTaskFactory;
    }

    void run() {

        BlockingQueue<String> pipelineQueue = new LinkedBlockingQueue<>();

        String[] markets = {
            "ETHBTC"
        };
        socketClient.subscribe(markets);

        int cpuCores = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(cpuCores);
        ExecutorService executorService = Executors.newCachedThreadPool();

        int period = 300;
        long now = Instant.now().getEpochSecond();
        long delay = (period - (now % period));
        for (String market : markets) {
            DataAggregatorTask dataAggregatorTask = dataAggregatorTaskFactory.newTaskForMarket(market, pipelineQueue);
            scheduledExecutorService.scheduleAtFixedRate(dataAggregatorTask, delay, period, TimeUnit.SECONDS);
        }

        while (true) {
            try {
                String market = pipelineQueue.take();
                IndicatorCalculationTask indicatorCalculationTask = indicatorCalculationTaskFactory.newTaskForMarket(market);
                executorService.submit(indicatorCalculationTask);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
