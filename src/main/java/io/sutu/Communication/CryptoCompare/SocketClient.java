package io.sutu.Communication.CryptoCompare;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.sutu.PipelineQueuesFactory;
import io.sutu.Trade;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SocketClient {

    private static final HashMap<String, String> marketsToSubscriptions;
    static {
        marketsToSubscriptions = new HashMap<>();
        marketsToSubscriptions.put("ETH-BTC", "2~BitTrex~ETH~BTC");
        marketsToSubscriptions.put("NEO-BTC", "2~BitTrex~NEO~BTC");
    }

    private PipelineQueuesFactory pipelineQueuesFactory;

    public SocketClient(PipelineQueuesFactory pipelineQueuesFactory) {
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public void subscribe(String[] markets) {
        List<BlockingQueue<Trade>> rawDataQueues = pipelineQueuesFactory.getRawDataQueues();

        URI uri = null;
        try {
            uri = new URI("https://streamer.cryptocompare.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Socket socket = IO.socket(uri);
        socket.on(Socket.EVENT_CONNECT, args -> {
            List<String> subscriptions = Stream.of(markets)
                    .map(SocketClient.marketsToSubscriptions::get)
                    .collect(Collectors.toList());

            HashMap<String, List<String>> emitParam = new HashMap<>();
            emitParam.put("subs", subscriptions);
            socket.emit("SubAdd", emitParam);
        }).on("m", args -> {
            Stream.of(args)
                    .filter(line -> line.toString().charAt(0) != '3')
                    .forEach(line -> {
                        Trade trade = unpack((String) line);
                        try {
                            for (BlockingQueue<Trade> rawDataQueue : rawDataQueues) {
                                rawDataQueue.put(trade);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        }).on(Socket.EVENT_DISCONNECT, args -> System.out.println(String.format("[%s] Disconnected", new Date())));

        socket.connect();
    }

    private Trade unpack(String message) {
        String[] split = message.split("~");

        Trade marketData = new Trade(
                split[2],
                split[3],
                Double.parseDouble(split[5]),
                Long.parseLong(split[6]),
                Double.parseDouble(split[8])
        );

        return marketData;
    }
}
