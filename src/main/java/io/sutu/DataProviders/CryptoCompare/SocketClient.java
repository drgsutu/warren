package io.sutu.DataProviders.CryptoCompare;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SocketClient {

    private static final HashMap<String, String> marketsToSubscriptions;
    static {
        marketsToSubscriptions = new HashMap<>();
        marketsToSubscriptions.put("ETHBTC", "2~BitTrex~ETH~BTC");
    }

    private StorageFactory storageFactory;

    public SocketClient(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public void subscribe(String[] markets) {
        String hostName = "https://streamer.cryptocompare.com";
        List<String> subscriptions = Stream.of(markets)
                .map(SocketClient.marketsToSubscriptions::get)
                .collect(Collectors.toList());

        try {
            Socket socket = IO.socket(hostName);
            socket.on(Socket.EVENT_CONNECT, args -> {

                HashMap<String, List<String>> emitParam = new HashMap<>();
                emitParam.put("subs", subscriptions);
                socket.emit("SubAdd", emitParam);

            }).on("m", args -> {

                Stream.of(args)
                        .filter(line -> line.toString().charAt(0) != '3')
                        .forEach(line -> {
                            MarketData marketData = unpack((String) line);
                            Deque<MarketData> storage = storageFactory.newStorageForMarket(marketData.getMarket());
                            storage.addFirst(marketData);
                        });

            }).on(Socket.EVENT_DISCONNECT, args -> System.out.println("Disconnected"));

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private MarketData unpack(String message) {
        String[] split = message.split("~");

        MarketData marketData = new MarketData(
                split[2],
                split[3],
                Double.parseDouble(split[5]),
                Long.parseLong(split[6]),
                Double.parseDouble(split[7]),
                Double.parseDouble(split[8])
        );

        return marketData;
    }
}
