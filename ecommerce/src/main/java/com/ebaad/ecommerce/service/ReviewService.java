package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.model.Review;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;

    public List<Review> getAllReview(Long productid);
}
