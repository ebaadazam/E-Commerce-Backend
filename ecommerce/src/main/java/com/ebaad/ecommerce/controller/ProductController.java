package com.ebaad.ecommerce.controller;

import com.ebaad.ecommerce.exception.ProductException;
import com.ebaad.ecommerce.model.Product;
import com.ebaad.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Finds Product By Category and we can do filter the product based on the various features
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
    @RequestParam List<String> color, @RequestParam List<String> size, @RequestParam Integer minPrice,
    @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort,
     @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize)
    {
        Page<Product> res = productService.getAllProduct(category,color,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize);
        System.out.println("Complete Products");

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }


    // Finds Product By Id
    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException{
        Product product = productService.findProductById(productId);

        return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
    }

}
