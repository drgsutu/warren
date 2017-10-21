package io.sutu.warren.Communication.Kraken;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class HttpClient {

    private Gson gson;

    public HttpClient(Gson gson) {
        this.gson = gson;
    }

    public HttpResponseResult getOHCLVData(String pair, int interval, Long since) {
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
                System.out.println(httpResponse.error);
            }

            return httpResponse.result;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
