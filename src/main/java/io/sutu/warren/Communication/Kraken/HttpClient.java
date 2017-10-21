package io.sutu.warren.Communication.Kraken;

import com.google.gson.Gson;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HttpClient {

    private Gson gson;

    public HttpClient(Gson gson) {
        this.gson = gson;
    }

    public HttpResponseResult getOHCLVData(String pair, int interval, Long since) throws HttpClientException {
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.kraken.com")
                    .setPath("/0/public/OHLC")
                    .setParameter("pair", pair)
                    .setParameter("interval", Integer.toString(interval))
                    .setParameter("since", Long.toString(since))
                    .build();

            String rawResponse = Request.Get(uri).execute().returnContent().asString();
            HttpResponse httpResponse = gson.fromJson(rawResponse, HttpResponse.class);

            if (httpResponse.error.size() > 0) {
                throw new HttpClientException("API responded with error(s): \n" +
                        httpResponse.error.stream().collect(Collectors.joining("\n")));
            }

            return httpResponse.result;

        } catch (IOException e) {
            throw new HttpClientException("Error occurred during HTTP communication", e);
        } catch (URISyntaxException e) {
            throw new HttpClientException("Error in URI syntax", e);
        }
    }


    private class HttpResponse {

        private List<String> error;
        private HttpResponseResult result;

        public void setError(List<String> error) {
            this.error = error;
        }

        public void setResult(HttpResponseResult result) {
            this.result = result;
        }
    }
}
