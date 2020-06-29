package com.bridgelabz.bookstoreapp.dto;

public class WishlistDto {

    private int bookId;

    public WishlistDto(int bookId) {
        this.bookId = bookId;
    }

    public WishlistDto() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
