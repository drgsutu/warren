package io.sutu.warren.Communication.CryptoCompare;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.sutu.warren.PipelineQueuesFactory;
import io.sutu.warren.Trade;
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
        marketsToSubscriptions.put("XLM-BTC", "2~BitTrex~XLM~BTC");
        marketsToSubscriptions.put("XRP-BTC", "2~BitTrex~XRP~BTC");
        marketsToSubscriptions.put("BTC-USD", "2~Bitfinex~BTC~USD");
    }

    private PipelineQueuesFactory pipelineQueuesFactory;

    public SocketClient(PipelineQueuesFactory pipelineQueuesFactory) {
        this.pipelineQueuesFactory = pipelineQueuesFactory;
    }

    public void subscribe(String[] markets) {
        List<BlockingQueue<Trade>> tradesQueues = pipelineQueuesFactory.getTradesQueues();

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
//                    .peek(System.out::println)
                    .filter(line -> line.toString().charAt(0) != '3')
                    .forEach(line -> {
                        try {
                            Trade trade = unpack((String) line);
                            for (BlockingQueue<Trade> tradeQueue : tradesQueues) {
                                tradeQueue.put(trade);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            System.out.println("[ERROR] parsing: " + line);
                        }
                    });
        }).on(Socket.EVENT_DISCONNECT, args -> System.out.println(String.format("[%s] Disconnected", new Date())));

        socket.connect();
    }

    private Trade unpack(String message) throws NumberFormatException {
        String[] split = message.split("~");

        final String fromCurrency = split[2];
        final String toCurrency = split[3];
        final double price = Double.parseDouble(split[5]);
        final long timeStamp = Long.parseLong(split[6]);
        final double volume = Double.parseDouble(split[7]);

        return new Trade(fromCurrency, toCurrency, price, timeStamp, volume);
    }
}
