package com.bridgelabz.bookstoreapp.dto;

public class BookDto {

    private String author;
    private String nameOfBook;
    private String picPath;
    private int price;
    private String description;
    private int quantity;

    public BookDto() {
    }

    public BookDto(String author, String nameOfBook, String picPath, int price, String description) {
        this.author = author;
        this.nameOfBook = nameOfBook;
        this.picPath = picPath;
        this.price = price;
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
