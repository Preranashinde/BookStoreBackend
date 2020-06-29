package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.CartDto;
import com.bridgelabz.bookstoreapp.dto.CartQtyDto;
import com.bridgelabz.bookstoreapp.entity.Book;

import java.util.List;
import java.util.Map;

public interface ICartService {
    String addToCart(CartDto cartDto, String token) throws BookStoreException;

    String removeFromCart(CartDto cartDto, String token) throws BookStoreException;

    List<CartQtyDto> getCartBooks(String token);

    Integer getOrderId(String token) throws BookStoreException;
}
