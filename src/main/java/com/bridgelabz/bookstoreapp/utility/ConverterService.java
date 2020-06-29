package com.bridgelabz.bookstoreapp.utility;

import com.bridgelabz.bookstoreapp.dto.BookDto;
import com.bridgelabz.bookstoreapp.dto.CartDto;
import com.bridgelabz.bookstoreapp.dto.CustomerDetailsDto;
import com.bridgelabz.bookstoreapp.dto.WishlistDto;
import com.bridgelabz.bookstoreapp.entity.Book;
import com.bridgelabz.bookstoreapp.entity.Cart;
import com.bridgelabz.bookstoreapp.entity.CustomerDetails;
import com.bridgelabz.bookstoreapp.entity.Wishlist;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConverterService {

    @Autowired
    private ModelMapper modelMapper;

    public BookDto convertToBookDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    public Book convertToBookEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }

    public CartDto convertToCartDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }

    public Cart convertToCartEntity(CartDto cartDto) {
        return modelMapper.map(cartDto, Cart.class);
    }

    public WishlistDto converToWishlistDto(Wishlist wishlist) {
        return modelMapper.map(wishlist, WishlistDto.class);
    }

    public Wishlist convertToWishlistEntity(WishlistDto wishlistDto) {
        return modelMapper.map(wishlistDto, Wishlist.class);
    }

    public CustomerDetailsDto converToCustomerDetailsDto(CustomerDetails customerDetails) {
        return modelMapper.map(customerDetails, CustomerDetailsDto.class);
    }

    public CustomerDetails converToCustomerDetailsEntity(CustomerDetailsDto customerDetailsDto) {
        return modelMapper.map(customerDetailsDto, CustomerDetails.class);
    }
}