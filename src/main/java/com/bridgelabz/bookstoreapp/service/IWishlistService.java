package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.WishlistDto;
import com.bridgelabz.bookstoreapp.entity.Book;
import com.bridgelabz.bookstoreapp.entity.Wishlist;

import java.util.List;

public interface IWishlistService {

    String addToWishlist(WishlistDto wishlistDto, String token) throws BookStoreException;

    String removeFromWishlist(WishlistDto wishlistDto, String token) throws BookStoreException;

    List<Book> getAllBooksList(String token) throws BookStoreException;
}
