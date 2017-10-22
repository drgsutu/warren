package io.sutu.warren.communication.bittrex.v2;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClient {

    public String getTicker(String market) throws HttpClientException {
        URI url = buildURLWithParams(market);

        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (IOException e) {
            throw new HttpClientException(e.getMessage(), e);
        }
    }

    private URI buildURLWithParams(String market) throws HttpClientException {
        try {
            URIBuilder builder = new URIBuilder()
                    .setScheme("https")
                    .setHost("bittrex.com")
                    .setPath("/Api/v2.0/pub/market/GetLatestTick")
                    .setParameter("marketName", market)
                    .setParameter("tickInterval", "oneMin");

            return builder.build();

        } catch (URISyntaxException e) {
            throw new HttpClientException(e.getMessage(), e);
        }
    }
}
