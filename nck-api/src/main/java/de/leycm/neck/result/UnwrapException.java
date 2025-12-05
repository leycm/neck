package de.leycm.neck.result;

public class UnwrapException extends RuntimeException {

    public UnwrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnwrapException(Throwable cause) {
        super("Unwrap a Result with an Exception", cause);
    }

}
