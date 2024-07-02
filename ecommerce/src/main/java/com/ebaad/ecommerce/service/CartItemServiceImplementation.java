package com.ebaad.ecommerce.service;

import com.ebaad.ecommerce.exception.CartItemException;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.Cart;
import com.ebaad.ecommerce.model.CartItem;
import com.ebaad.ecommerce.model.Product;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.repository.CartItemRepository;
import com.ebaad.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImplementation  implements CartItemService{
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final CartRepository cartRepository;

    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepository.save(cartItem);

        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());
        if (user.getId() == userId){
            item.setQuantity(1);
            item.setPrice(item.getProduct().getPrice() * item.getQuantity());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem = cartItemRepository.isCartItemExist(cart, product, size, userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemId);
        User user = userService.findUserById(cartItem.getUserId());

        // For logged in user
        User reqUser = userService.findUserById(userId);

        if (user.getId() == reqUser.getId()){
            cartItemRepository.deleteById(cartItemId);
        } else{
            throw new UserException("Alert! You Can't Remove Another User's Item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new CartItemException("Cart Item not found with Id: " + cartItemId);
    }
}
