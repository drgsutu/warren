package io.sutu.warren.communication.kraken;

public class HttpClientException extends Exception {

    HttpClientException(String message) {
        super(message);
    }

    HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
