package de.n26.exception;

public class TransactionExistException extends RuntimeException {

    private static final long serialVersionUID = 2016_09_24L;

    public TransactionExistException(String message) {
        super(message);
    }

}
