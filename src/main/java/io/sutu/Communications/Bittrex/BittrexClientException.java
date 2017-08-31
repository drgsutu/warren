package io.sutu.Communications.Bittrex;

public class BittrexClientException extends Exception {

    BittrexClientException(String message, Exception cause) {
        super(message, cause);
    }
}
