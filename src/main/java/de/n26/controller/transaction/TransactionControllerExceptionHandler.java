package de.n26.controller.transaction;

import de.n26.exception.TransactionExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice(basePackages = "de.n26.controller.transaction")
public class TransactionControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cant find transaction with passed id")
    @ExceptionHandler(NoSuchElementException.class)
    public void noSuchElementExceptionHandler(NoSuchElementException ex) {
        logger.warn(ex.getMessage(), ex);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Transaction with passed id already exists")
    @ExceptionHandler(TransactionExistException.class)
    public void transactionExistExceptionHandler(TransactionExistException ex) {
        logger.warn(ex.getMessage(), ex);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Oops something went wrong, please try again later")
    @ExceptionHandler(Exception.class)
    public void internalServerErrorHandler(Exception ex) {
        logger.error(ex.getMessage(), ex);
    }
}
