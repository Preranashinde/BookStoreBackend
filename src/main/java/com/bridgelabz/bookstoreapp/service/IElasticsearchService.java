package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.entity.Book;

import java.io.IOException;

public interface IElasticsearchService {

    public String createBook(Book book)throws IOException;

}
