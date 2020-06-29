package com.bridgelabz.bookstoreapp.repository;

import com.bridgelabz.bookstoreapp.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {
    Boolean existsByUsername(String username);
}
