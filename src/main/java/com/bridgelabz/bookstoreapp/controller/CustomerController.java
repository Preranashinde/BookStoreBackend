package com.bridgelabz.bookstoreapp.controller;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.CustomerDetailsDto;
import com.bridgelabz.bookstoreapp.service.ICustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home/customer")
@CrossOrigin(value = "*")
public class CustomerController {

    @Autowired
    private ICustomerDetailsService iCustomerDetailsService;

    @PostMapping("/adddetails")
    public ResponseEntity<String> addCustomerDetails(@RequestBody CustomerDetailsDto customerDetailsDto, @RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<String>(iCustomerDetailsService.addCustomerDetails(customerDetailsDto, token), HttpStatus.OK);
    }

    @GetMapping("/isexisted")
    public ResponseEntity<Boolean> isCustomerDetailsExisted(@RequestHeader String token) throws BookStoreException {
        return new ResponseEntity<Boolean>(iCustomerDetailsService.isCustomerDetailsExisted(token), HttpStatus.OK);
    }

}
