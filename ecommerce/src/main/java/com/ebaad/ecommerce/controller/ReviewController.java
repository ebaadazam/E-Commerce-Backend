package com.ebaad.ecommerce.controller;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.Review;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.request.ReviewRequest;
import com.ebaad.ecommerce.service.ReviewService;
import com.ebaad.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    // Creating a review for a product
    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(req, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    // We provide product id and it will return all the reviews related to the particular product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReview(@PathVariable Long productId) throws UserException,ProductException{
        List<Review> reviews = reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviews, HttpStatus.ACCEPTED);
    }
}
