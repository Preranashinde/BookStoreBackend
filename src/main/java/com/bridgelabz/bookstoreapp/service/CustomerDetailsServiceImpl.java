package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.Exception.BookStoreException;
import com.bridgelabz.bookstoreapp.dto.CustomerDetailsDto;
import com.bridgelabz.bookstoreapp.entity.CustomerDetails;
import com.bridgelabz.bookstoreapp.repository.CustomerDetailsRepository;
import com.bridgelabz.bookstoreapp.utility.ConverterService;
import com.bridgelabz.bookstoreapp.utility.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:message.properties")
public class CustomerDetailsServiceImpl implements ICustomerDetailsService {

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Environment environment;

    @Override
    public String addCustomerDetails(CustomerDetailsDto customerDetailsDto, String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            CustomerDetails customerDetails = converterService.converToCustomerDetailsEntity(customerDetailsDto);
            customerDetails.setUsername(username);
            customerDetailsRepository.save(customerDetails);
            return environment.getProperty("CUSTOMER_DETAILS_ADDED");
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }

    @Override
    public Boolean isCustomerDetailsExisted(String token) throws BookStoreException {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            return customerDetailsRepository.existsByUsername(username);
        } else
            throw new BookStoreException(BookStoreException.ExceptionType.JWT_NOT_VALID, environment.getProperty("JWT_NOT_VALID"));
    }
}
