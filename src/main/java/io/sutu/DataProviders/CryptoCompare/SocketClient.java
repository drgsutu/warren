package io.sutu.DataProviders.CryptoCompare;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.sutu.Storage.StorageFactory;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

@Component
public class SocketClient {

    private StorageFactory storageFactory;

    public SocketClient(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public void subscribe(String[] subscriptions) {
        String hostName = "https://streamer.cryptocompare.com";

        try {
            Socket socket = IO.socket(hostName);
            socket.on(Socket.EVENT_CONNECT, args -> {

                HashMap<String, List<String>> emitParam = new HashMap<>();
                emitParam.put("subs", Arrays.asList(subscriptions));
                socket.emit("SubAdd", emitParam);

            }).on("m", args -> {

                Stream.of(args)
                        .filter(line -> line.toString().charAt(0) != '3')
                        .forEach(line -> {
                            System.out.println(line);
                            MarketData marketData = unpack((String) line);
                            List storage = storageFactory.newStorageForMarket(marketData.getMarket());
                            storage.add(marketData);
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
