package org.example.exceptions;

public class SolverException extends MainException {
    public SolverException() {
        super();
    }

    public SolverException(String message) {
        super(message);
    }

    public SolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
