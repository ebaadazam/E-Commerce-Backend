package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.model.Cart;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;

    public Cart findUserCart(Long userId);
}
