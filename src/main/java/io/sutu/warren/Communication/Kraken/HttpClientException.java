package io.sutu.warren.Communication.Kraken;

public class HttpClientException extends Exception {

    HttpClientException(String message) {
        super(message);
    }

    HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
