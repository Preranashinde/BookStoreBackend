package com.bridgelabz.bookstoreapp.Exception;

public class BookStoreException extends Exception {

    public enum ExceptionType {
        CUSTOMER_DETAILS_EXISTED, JWT_NOT_VALID, QUANTITY_EXCEEDED
    }

    public ExceptionType type;

    public BookStoreException(ExceptionType type, String message) {
        super(message);
        this.type = type;
    }
}
