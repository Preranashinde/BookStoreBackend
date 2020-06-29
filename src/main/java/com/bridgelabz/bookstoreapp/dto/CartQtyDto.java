package com.bridgelabz.bookstoreapp.dto;

import java.io.Serializable;

public class CartQtyDto implements Serializable {

    private int id;
    private String author;
    private String nameOfBook;
    private String picPath;
    private int price;
    private int bookQuantity;

    public CartQtyDto(int id, String author, String nameOfBook, String picPath, int price, int bookQuantity) {
        this.id = id;
        this.author = author;
        this.nameOfBook = nameOfBook;
        this.picPath = picPath;
        this.price = price;
        this.bookQuantity = bookQuantity;
    }

    public CartQtyDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }
}
