package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.CustomerDetailsDto;

public interface ICustomerDetailsService {

    String addCustomerDetails(CustomerDetailsDto customerDetailsDto, String token) throws BookStoreException;

    Boolean isCustomerDetailsExisted(String token) throws BookStoreException;
}
