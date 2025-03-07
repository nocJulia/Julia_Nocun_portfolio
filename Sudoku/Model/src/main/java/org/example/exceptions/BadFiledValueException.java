package org.example.exceptions;

public class BadFiledValueException extends IllegalArgumentException {
    public BadFiledValueException(final String message) {
        super(message);
    }

}
