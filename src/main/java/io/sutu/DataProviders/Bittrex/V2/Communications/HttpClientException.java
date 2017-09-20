package io.sutu.DataProviders.Bittrex.V2.Communications;

public class HttpClientException extends Exception {

    HttpClientException(String message, Exception cause) {
        super(message, cause);
    }
}
