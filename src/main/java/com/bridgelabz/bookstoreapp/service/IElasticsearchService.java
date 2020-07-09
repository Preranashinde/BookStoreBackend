package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.entity.Book;

import java.io.IOException;
import java.util.List;

public interface IElasticsearchService {

    public String createBook(Book book) throws IOException;

    public List<Book> searchBook(String nameOfBook) throws IOException;
    public Book updateBook(int id, Book book)throws IOException;
    public String deleteBook(int id)throws IOException;
}
