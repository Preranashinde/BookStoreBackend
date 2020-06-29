package com.bridgelabz.bookstoreapp.repository;

import com.bridgelabz.bookstoreapp.dto.CartDto;
import com.bridgelabz.bookstoreapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    boolean existsCartByUsername(String username);
    boolean existsCartByBookId(int bookId);
    List<Cart> findAllByUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart cart WHERE cart.bookId = :bookId AND cart.username = :username")
    void deleteCartByBookIdAndUsername(@Param("bookId") int bookId, @Param("username") String username);

    Cart findByUsername(String username);

    Cart findByBookId(int bookId);

    Cart findByBookIdAndUsername(int bookId,String username);
}
