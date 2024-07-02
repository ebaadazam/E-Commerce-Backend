package com.ebaad.ecommerce.request;

public class ReviewRequest {

    private long productId;
    private String review;

    // Getter Setter
    public long getProductId() {
        return productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
}
