package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.model.Product;
import com.ebaad.ecommerce.model.Review;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.repository.ProductRepository;
import com.ebaad.ecommerce.repository.ReviewRepository;
import com.ebaad.ecommerce.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService{
    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ProductRepository productRepository;

    public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        // product to give review of
        Product product = productService.findProductById(req.getProductId());
        Review review = new Review();
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productid) {
        return List.of();
    }
}
