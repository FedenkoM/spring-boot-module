package org.example.exception;

public class KeyPairException extends RuntimeException{

    public KeyPairException(String message) {
        super(message);
    }

    public KeyPairException(Throwable throwable) {
        super(throwable);
    }
}
