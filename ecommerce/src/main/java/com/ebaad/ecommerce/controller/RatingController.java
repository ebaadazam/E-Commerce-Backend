package com.ebaad.ecommerce.controller;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.Rating;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.request.RatingRequest;
import com.ebaad.ecommerce.service.RatingService;
import com.ebaad.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(req, user);
        return new ResponseEntity<Rating>(rating, HttpStatus.CREATED);
    }

    // We provide product id and it will return all the ratings related to the particular product
    @GetMapping("/product/{productId}")
    public ResponseEntity<Rating> getProductRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        User user = userService.findUserProfileByJwt(jwt); //jwt token
        Rating rating = ratingService.createRating(req, user);
        return new ResponseEntity<Rating>(rating, HttpStatus.CREATED);
    }
}
