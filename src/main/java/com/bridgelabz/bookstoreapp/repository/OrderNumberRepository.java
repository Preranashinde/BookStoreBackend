package com.bridgelabz.bookstoreapp.repository;

import com.bridgelabz.bookstoreapp.entity.OrderNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNumberRepository extends JpaRepository<OrderNumber, Integer> {
    OrderNumber findFirstByOrderByIdDesc();
}
