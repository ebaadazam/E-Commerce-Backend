package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;
}
