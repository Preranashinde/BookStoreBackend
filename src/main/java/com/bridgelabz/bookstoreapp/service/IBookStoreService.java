package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.dto.BookDto;


public interface IBookStoreService {

    void loadBookData();
    String addNewBook(BookDto bookDto);
}
