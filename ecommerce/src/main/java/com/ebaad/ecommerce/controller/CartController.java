package com.ebaad.ecommerce.controller;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.exception.UserException;
import com.ebaad.ecommerce.model.Cart;
import com.ebaad.ecommerce.model.User;
import com.ebaad.ecommerce.request.AddItemRequest;
import com.ebaad.ecommerce.response.ApiResponse;
import com.ebaad.ecommerce.service.CartService;
import com.ebaad.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // Find the user cart by User Id
    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    // Add an Item to the cart
    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
    @RequestHeader("Authorization") String jwt) throws UserException,ProductException{
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getId(), req);
        ApiResponse res = new ApiResponse();
        res.setMessage("Item Added to Cart");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
