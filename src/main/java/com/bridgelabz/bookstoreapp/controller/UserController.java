package com.bridgelabz.bookstoreapp.controller;

import com.bridgelabz.bookstoreapp.entity.Book;
import com.bridgelabz.bookstoreapp.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private IBookStoreService iBookStoreService;

    @GetMapping("/allbook")
    public ResponseEntity<Page<Book>> getAllBook(@PageableDefault(page = 0, size = 10) Pageable pageable, @RequestHeader String Authorization) {
        return new ResponseEntity(iBookStoreService.getAllBook(pageable), HttpStatus.OK);
    }

    @GetMapping("/{searchText}")
    public ResponseEntity<Book> booksByAuthor(@PathVariable String searchText, @RequestHeader String Authorization) {
        return new ResponseEntity(iBookStoreService.searchBooks(searchText), HttpStatus.OK);
    }

    @GetMapping("/sort-asc/price")
    public ResponseEntity<Page<Book>> booksInAscendingOrderByPrice(@PageableDefault(page = 0, size = 10) Pageable pageable, @RequestHeader String Authorization) {
        return new ResponseEntity(iBookStoreService.getAllBookByPriceAsc(pageable), HttpStatus.OK);
    }

    @GetMapping("/sort-desc/price")
    public ResponseEntity<Page<Book>> booksInDescendingOrderByPrice(@PageableDefault(page = 0, size = 10) Pageable pageable, @RequestHeader String Authorization) {
        return new ResponseEntity(iBookStoreService.getAllBookByPriceDesc(pageable), HttpStatus.OK);
    }
}