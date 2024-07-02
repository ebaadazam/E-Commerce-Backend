package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.config.JwtProvider;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        }
        throw new UserException("User Not Found with Id: " + userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new UserException("User Not Found with email: " + email);
        }
        return user;
    }
}
