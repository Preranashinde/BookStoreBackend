package com.bridgelabz.bookstoreapp.dto;

public class CartDto {

    private int bookId;
    private int bookQuantity;

    public CartDto(int bookId, int bookQuantity) {
        this.bookId = bookId;
        this.bookQuantity = bookQuantity;
    }

    public CartDto() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }
}