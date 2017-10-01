package io.sutu.Communication.Bittrex.V2;

public class HttpClientException extends Exception {

    HttpClientException(String message, Exception cause) {
        super(message, cause);
    }
}
