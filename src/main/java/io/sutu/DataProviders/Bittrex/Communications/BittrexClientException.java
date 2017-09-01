package io.sutu.DataProviders.Bittrex.Communications;

public class BittrexClientException extends Exception {

    BittrexClientException(String message, Exception cause) {
        super(message, cause);
    }
}
