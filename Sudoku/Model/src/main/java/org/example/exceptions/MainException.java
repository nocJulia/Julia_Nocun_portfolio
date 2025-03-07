package org.example.exceptions;

import java.util.ResourceBundle;

public class MainException extends Exception {

    private static final ResourceBundle message = ResourceBundle.getBundle("message");

    public MainException() {
        super(message.getString("exceptionSudoku"));
    }

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }
}

