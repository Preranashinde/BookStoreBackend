package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.WishlistDto;
import com.bridgelabz.bookstoreapp.entity.Book;
import com.bridgelabz.bookstoreapp.entity.Wishlist;
import com.bridgelabz.bookstoreapp.repository.BookStoreRepository;
import com.bridgelabz.bookstoreapp.repository.WishlistRepository;
import com.bridgelabz.bookstoreapp.utility.ConverterService;
import com.bridgelabz.bookstoreapp.utility.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@PropertySource("classpath:message.properties")
public class WishlistServiceImpl implements IWishlistService {

    @Autowired
    private ConverterService converterService;

    @Autowired
    private BookStoreRepository bookStoreRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Environment environment;

    @Override
    public String addToWishlist(WishlistDto wishlistDto, String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            Wishlist wishlist = converterService.convertToWishlistEntity(wishlistDto);
            wishlist.setUsername(username);
            if (wishlistRepository.existsWishlistByUsername(wishlist.getUsername()))
                wishlistRepository.deleteWishlistByBookIdAndUsername(wishlist.getBookId(), wishlist.getUsername());
            wishlistRepository.save(wishlist);
            return environment.getProperty("ADDED_TO_WISHLIST");
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    @Override
    public String removeFromWishlist(WishlistDto wishlistDto, String token) throws  BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            Wishlist wishlist = converterService.convertToWishlistEntity(wishlistDto);
            wishlist.setUsername(username);
            wishlistRepository.deleteWishlistByBookIdAndUsername(wishlist.getBookId(), wishlist.getUsername());
            return environment.getProperty("REMOVED_FROM_WISHLIST");
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    @Override
    public List<Book> getAllBooksList(String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            List<Book> wishlistBooks = new ArrayList<>();
            List<Wishlist> allByUsername = wishlistRepository.findAllByUsername(username);
            for (Wishlist wishlist : allByUsername) {
                wishlistBooks.add(bookStoreRepository.findById(wishlist.getBookId()));
            }
            return wishlistBooks;
        }  else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));

    }
}
