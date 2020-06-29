package com.bridgelabz.bookstoreapp.controller;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.CartDto;
import com.bridgelabz.bookstoreapp.dto.CartQtyDto;
import com.bridgelabz.bookstoreapp.entity.Book;
import com.bridgelabz.bookstoreapp.entity.Cart;
import com.bridgelabz.bookstoreapp.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("home/user/cart")
@CrossOrigin(value = "*")
public class CartController {

    @Autowired
    private ICartService iCartService;

    //    @CacheEvict(value = "Book", key = "#cartDto.userId")
    @PutMapping("/add")
    public String addToCart(@RequestBody CartDto cartDto, @RequestHeader String token) throws BookStoreException {
//        System.out.println(token);
        System.out.println("Updating records");
        return iCartService.addToCart(cartDto, token);
    }

    //    @CacheEvict(value = "Book", key = "#cartDto.userId")
    @PutMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody CartDto cartDto, @RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<String>(iCartService.removeFromCart(cartDto, token), HttpStatus.OK);
    }

    //    @Cacheable(value = "Book", key = "#userId")
    @GetMapping("/getall")
    public List<CartQtyDto> getall(@RequestHeader String token) {
//        System.out.println(Authorization);
        System.out.println("getting books from cart");
        return iCartService.getCartBooks(token);
    }

    @GetMapping("/orderplaced/orderid")
    public ResponseEntity<Integer> getOrderId(@RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<Integer>(iCartService.getOrderId(token), HttpStatus.OK);
    }
}
