package com.ebaad.ecommerce.controller;

import com.ebaad.ecommerce.exception.CartItemException;
import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.Cart;
import com.ebaad.ecommerce.model.CartItem;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.response.ApiResponse;
import com.ebaad.ecommerce.service.CartItemService;
import com.ebaad.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item Removed From Cart Successfully!");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @RequestBody CartItem cartItem,
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {

        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);

        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }
}
