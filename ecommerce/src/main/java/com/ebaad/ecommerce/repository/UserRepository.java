package com.ebaad.ecommerce.repository;

import com.ebaad.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    // Custom method implementation for findUserByEmail
    public User findUserByEmail(String email);
}
