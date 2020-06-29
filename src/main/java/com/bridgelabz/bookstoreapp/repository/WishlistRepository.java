package com.bridgelabz.bookstoreapp.repository;

import com.bridgelabz.bookstoreapp.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    boolean existsWishlistByUsername(String username);
    boolean existsWishlistByBookId(int bookId);
    List<Wishlist> findAllByUsername(String username);


    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist wishlist WHERE wishlist.bookId = :bookId AND wishlist.username = :username")
    void deleteWishlistByBookIdAndUsername(@Param("bookId") int bookId, @Param("username") String username);
}
