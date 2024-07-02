package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.CartItemException;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.Cart;
import com.ebaad.ecommerce.model.CartItem;
import com.ebaad.ecommerce.model.Product;
import jdk.jshell.spi.ExecutionControl;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
