package io.sutu.DataProviders.Bittrex.Communications;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class BittrexClient {

    public String getTicker(String market) throws BittrexClientException {
        URI url = buildURLWithParams(market);

        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (IOException e) {
            throw new BittrexClientException(e.getMessage(), e);
        }
    }

    private URI buildURLWithParams(String market) throws BittrexClientException {
        try {
            URIBuilder builder = new URIBuilder()
                    .setScheme("https")
                    .setHost("bittrex.com")
                    .setPath("/api/v1.1/public/getticker")
                    .setParameter("market", market);

            return builder.build();

        } catch (URISyntaxException e) {
            throw new BittrexClientException(e.getMessage(), e);
        }
    }
}
