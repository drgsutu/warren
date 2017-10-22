package io.sutu.warren.communication.bittrex.v2;

public class HttpClientException extends Exception {

    HttpClientException(String message, Exception cause) {
        super(message, cause);
    }
}
