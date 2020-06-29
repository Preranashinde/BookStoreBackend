package com.bridgelabz.bookstoreapp.controller;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.WishlistDto;
import com.bridgelabz.bookstoreapp.entity.Book;
import com.bridgelabz.bookstoreapp.service.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home/user/wishlist")
@CrossOrigin(value = "*")
public class WishlistController {

    @Autowired
    private IWishlistService iWishlistService;

    //    @CacheEvict(value = "Book", key = "#cartDto.userId")
    @PutMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestBody WishlistDto wishlistDto, @RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<String>(iWishlistService.addToWishlist(wishlistDto, token), HttpStatus.OK);
    }

    @PutMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestBody WishlistDto wishlistDto, @RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<String>(iWishlistService.removeFromWishlist(wishlistDto, token), HttpStatus.OK);
    }

    //    @Cacheable(value = "Book", key = "#userId")
    @GetMapping("/getall")
    public ResponseEntity<List<Book>> getAllBooksList(@RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<List<Book>>(iWishlistService.getAllBooksList(token), HttpStatus.OK);
    }
}
